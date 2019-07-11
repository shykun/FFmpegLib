package com.shykun.volodymyr.videoeditor.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.shykun.volodymyr.ffmpeglib.ContentType
import com.shykun.volodymyr.ffmpeglib.ffmpeg.FFMpegCallback
import com.shykun.volodymyr.ffmpeglib.ffmpeg.video.AddedText
import com.shykun.volodymyr.videoeditor.*
import com.shykun.volodymyr.videoeditor.dialog.AddTextDialog
import com.shykun.volodymyr.videoeditor.dialog.OnTextEditorListener
import com.shykun.volodymyr.videoeditor.usecase.AddTextUseCase
import kotlinx.android.synthetic.main.fragment_add_text.*
import kotlinx.android.synthetic.main.view_added_text.view.*
import java.io.File


const val ADD_TEXT_DIALOG_TAG = "add_text_dialog"

class AddTextFragment : Fragment(), OnTextEditorListener, View.OnTouchListener, OnScaleGestureListener, FFMpegCallback {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private lateinit var progressDialog: AlertDialog

    val addedTextList = ArrayList<AddedText?>()
    var _xDelta = 0
    var _yDelta = 0
    var selectedView: View? = null

    override fun onStartProcessing() {
        progressDialog.show()
    }

    override fun onProgress(progress: String) {
        progressDialog.setMessage("progress : $progress")
    }

    override fun onSuccess(convertedFile: File, contentType: ContentType) {
        Toast.makeText(context, getString(R.string.success), Toast.LENGTH_SHORT).show()
        openResult(context, convertedFile, "video/mp4")
    }

    override fun onFailure(error: Exception) {
        Toast.makeText(context, getString(R.string.failure), Toast.LENGTH_SHORT).show()
    }

    override fun onNotAvailable(error: Exception) {
        Toast.makeText(context, getString(R.string.not_available), Toast.LENGTH_SHORT).show()
    }

    override fun onFinishProcessing() {
        progressDialog.hide()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProviders.of(activity!!)
            .get(MainViewModel::class.java)
        scaleGestureDetector = ScaleGestureDetector(this)
        progressDialog = getProgressDialog(context!!)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_text, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnAddTextClickListener()
        setRootClickListener()
        setRootTouchListener()
        setOnSaveButtonListener()
        setupVideo()
    }

    private fun setOnAddTextClickListener() = addTextFAB.setOnClickListener {
        showDialog()
    }


    private fun showDialog(text: String = "", colorCode: Int = R.color.white_color_picker) {
        val addTextDialog = AddTextDialog.newInstance(text, colorCode)
        addTextDialog.onTextEditorListener = this
        addTextDialog.show(childFragmentManager, ADD_TEXT_DIALOG_TAG)
    }

    private fun setRootClickListener() {
        addTextRoot.setOnClickListener {
            hideFrameOfSelectedView()
            selectedView = null
        }
    }

    private fun setRootTouchListener() {
        var startX = 0f
        var startY = 0f
        var endX = 0f
        var endY = 0f
        addTextRoot.setOnTouchListener { view, event ->
            scaleGestureDetector.onTouchEvent(event)

            if (event.actionMasked == MotionEvent.ACTION_UP) {
                startX = event.x
                startY = event.y
            }

            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                endX = event.x
                endY = event.y
            }

            if (isAClick(startX, startY, endX, endY))
                view.performClick()

            true
        }
    }

    private fun isAClick(startX: Float, startY: Float, endX: Float, endY: Float): Boolean {
        return Math.abs(startX - endX) < 5 && Math.abs(startY - endY) < 5
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
        view.tag = addedTextList.size

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
                addedTextList[view.tag as Int] = null
                selectedView = null
            }

            setOnTouchListener(this@AddTextFragment)

            addTextRoot.addView(this)

            setOnLongClickListener {
                showDialog(inputText, colorCode)
                true
            }
        }

        val addedText = AddedText(inputText, colorCode, view.tvPhotoEditorText.textSize.toInt(), 0, 0)
        addedTextList.add(addedText)
    }

    private fun hideFrameOfSelectedView() {
        selectedView?.apply {
            frmBorder.setBackgroundResource(0)
            imgPhotoEditorClose.visibility = View.GONE
        }
        selectedView = null
    }

    private fun setOnSaveButtonListener() {
        addTextSaveButton.setOnClickListener {
            AddTextUseCase(
                activity!!,
                mainViewModel.selectedVideoUri.value!!,
                addedTextList,
                this
            ).execute()
        }
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
                Log.d("POSITION", "${layoutParams.leftMargin} : ${layoutParams.topMargin}")

                addedTextList[v.tag as Int]?.apply {
                    x = X - _xDelta
                    y = Y - _yDelta
                }
            }
        }
        addTextRoot.invalidate()
        return true
    }

    override fun onScaled(ratio: Float) {
        selectedView?.tvPhotoEditorText?.textSize = ratio + 10
        addedTextList[selectedView?.tag as Int]?.textSize = ratio.toInt() + 10
    }
}

