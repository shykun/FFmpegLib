package com.shykun.volodymyr.videoeditor

import android.view.MotionEvent

private const val STEP = 200f

interface OnScaleGestureListener {
    fun onScaled(ratio: Float)
}

class ScaleGestureDetector(private val mListener: OnScaleGestureListener) {
    private var mRatio = 1.0f
    private var mBaseDist = 0
    private var mBaseRatio = 0.0f

    fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.pointerCount == 2) {
            val action = event.action
            val pureaction = action and MotionEvent.ACTION_MASK
            if (pureaction == MotionEvent.ACTION_POINTER_DOWN) {
                mBaseDist = getDistance(event)
                mBaseRatio = mRatio
            } else {
                val delta = (getDistance(event) - mBaseDist) / STEP
                val multi = Math.pow(2.0, delta.toDouble()).toFloat()
                mRatio = Math.min(1024.0f, Math.max(0.1f, mBaseRatio * multi))
                mListener.onScaled(mRatio)
            }
        }
        return true
    }

    internal fun getDistance(event: MotionEvent): Int {
        val dx = (event.getX(0) - event.getX(1)).toInt()
        val dy = (event.getY(0) - event.getY(1)).toInt()
        return Math.sqrt((dx * dx + dy * dy).toDouble()).toInt()
    }
}