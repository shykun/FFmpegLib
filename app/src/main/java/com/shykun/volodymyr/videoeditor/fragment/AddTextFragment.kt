package com.shykun.volodymyr.videoeditor.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.shykun.volodymyr.videoeditor.*
import com.shykun.volodymyr.videoeditor.dialog.AddTextDialog
import com.shykun.volodymyr.videoeditor.dialog.OnTextEditorListener
import kotlinx.android.synthetic.main.fragment_add_text.*
import kotlinx.android.synthetic.main.view_added_text.view.*

const val ADD_TEXT_DIALOG_TAG = "add_text_dialog"

class AddTextFragment : Fragment(), OnTextEditorListener, View.OnTouchListener, OnScaleGestureListener, OnRotationGestureListener {

    lateinit var mainViewModel: MainViewModel
    lateinit var scaleGestureDetector: ScaleGestureDetector
    lateinit var rotationGestureDetector: RotationGestureDetector

    var _xDelta = 0
    var _yDelta = 0

    var selectedView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProviders.of(activity!!)
            .get(MainViewModel::class.java)
        scaleGestureDetector = ScaleGestureDetector(this)
        rotationGestureDetector = RotationGestureDetector(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_text, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnAddTextClickListener()
        setRootClickListener()
        setRootTouchListener()
        setupVideo()
    }

    private fun setOnAddTextClickListener() = addTextFAB.setOnClickListener {
        val addTextDialog = AddTextDialog.newInstance()
        addTextDialog.onTextEditorListener = this
        addTextDialog.show(childFragmentManager, ADD_TEXT_DIALOG_TAG)
    }

    private fun setRootClickListener() {
        addTextRoot.setOnClickListener {
            hideFrameOfSelectedView()
        }
    }

    private fun setRootTouchListener() {
        addTextRoot.setOnTouchListener{ view, event ->
            if (event.actionMasked == MotionEvent.ACTION_UP)
                view.performClick()

            scaleGestureDetector.onTouchEvent(event)
            rotationGestureDetector.onTouchEvent(event)

            true
        }
    }

    private fun setupVideo() {
        addTextVideo.apply {
            setVideoURI(mainViewModel.selectedVideoUri.value)
            setOnPreparedListener {
                it.start()
            }
        }
    }

    override fun onDone(inputText: String, colorCode: Int) {
        val view = LayoutInflater.from(context).inflate(R.layout.view_added_text, addTextRoot, false)

        view.apply {
            this.tvPhotoEditorText.apply {
                text = inputText
                setTextColor(colorCode)
            }

            setOnClickListener {
                hideFrameOfSelectedView()
                selectedView = it
                it.frmBorder.setBackgroundResource(R.drawable.rounded_border_tv)
                it.imgPhotoEditorClose.visibility = View.VISIBLE
            }

            imgPhotoEditorClose.setOnClickListener {
                addTextRoot.removeView(view)
                selectedView = null
            }

            setOnTouchListener(this@AddTextFragment)

            addTextRoot.addView(this)
        }
    }



    private fun hideFrameOfSelectedView() {
        selectedView?.apply {
           frmBorder.setBackgroundResource(0)
            imgPhotoEditorClose.visibility = View.GONE
        }
        selectedView = null
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val X = event.rawX.toInt()
        val Y = event.rawY.toInt()
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                val lParams = v.layoutParams as RelativeLayout.LayoutParams
                _xDelta = X - lParams.leftMargin
                _yDelta = Y - lParams.topMargin
                Log.d("ACTION_DOWN", lParams.leftMargin.toString() + " : " + lParams.topMargin)
            }
            MotionEvent.ACTION_UP -> {
                v.performClick()
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
            }
            MotionEvent.ACTION_POINTER_UP -> {
            }
            MotionEvent.ACTION_MOVE -> {
                val layoutParams = v.layoutParams as RelativeLayout.LayoutParams


                layoutParams.leftMargin = X - _xDelta
                layoutParams.topMargin = Y - _yDelta
                layoutParams.rightMargin = -250
                layoutParams.bottomMargin = -250
                v.layoutParams = layoutParams
            }
        }
        addTextRoot.invalidate()
        return true
    }

    override fun onScaled(ratio: Float) {
        selectedView?.tvPhotoEditorText?.textSize = ratio + 10
    }

    override fun onRotated(angle: Float) {
        selectedView?.rotation = angle
    }
}
