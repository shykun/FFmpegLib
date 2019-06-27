package com.shykun.volodymyr.videoeditor.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.shykun.volodymyr.videoeditor.MainViewModel
import com.shykun.volodymyr.videoeditor.R
import com.shykun.volodymyr.videoeditor.dialog.AddTextDialog
import com.shykun.volodymyr.videoeditor.dialog.OnTextEditorListener
import kotlinx.android.synthetic.main.fragment_add_text.*
import kotlinx.android.synthetic.main.view_added_text.view.*

const val ADD_TEXT_DIALOG_TAG = "add_text_dialog"

class AddTextFragment : Fragment(), OnTextEditorListener {

    lateinit var mainViewModel: MainViewModel

    var selectedView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProviders.of(activity!!)
            .get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_text, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnAddTextClickListener()
        setOnRootClickListener()
        setupVideo()
    }

    private fun setOnAddTextClickListener() = addTextFAB.setOnClickListener {
        val addTextDialog = AddTextDialog.newInstance()
        addTextDialog.onTextEditorListener = this
        addTextDialog.show(childFragmentManager, ADD_TEXT_DIALOG_TAG)
    }

    private fun setOnRootClickListener() {
        addTextRoot.setOnClickListener {
            hideFrameOfSelectedView()
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
        view.tvPhotoEditorText.apply {
            text = inputText
            setTextColor(colorCode)
        }

        val layoutParams = view.layoutParams as RelativeLayout.LayoutParams
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)

        addTextRoot.addView(view, layoutParams)

        view.setOnClickListener {
            selectedView = it
            it.frmBorder.setBackgroundResource(R.drawable.rounded_border_tv)
            it.imgPhotoEditorClose.visibility = View.VISIBLE
        }

        view.imgPhotoEditorClose.setOnClickListener {
            addTextRoot.removeView(view)
            selectedView = null
        }
    }

    private fun hideFrameOfSelectedView() {
        selectedView?.apply {
           frmBorder.setBackgroundResource(0)
            imgPhotoEditorClose.visibility = View.GONE
        }
    }

}
