package com.shykun.volodymyr.videoeditor.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.shykun.volodymyr.videoeditor.MainViewModel
import com.shykun.volodymyr.videoeditor.R
import com.shykun.volodymyr.videoeditor.dialog.AddTextDialog
import com.shykun.volodymyr.videoeditor.dialog.OnTextEditorListener
import kotlinx.android.synthetic.main.fragment_add_text.*

const val ADD_TEXT_DIALOG_TAG = "add_text_dialog"

class AddTextFragment : Fragment(), OnTextEditorListener {

    lateinit var mainViewModel: MainViewModel

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
        setupVideo()
    }

    private fun setOnAddTextClickListener() = addTextFAB.setOnClickListener {
        val addTextDialog = AddTextDialog.newInstance()
        addTextDialog.onTextEditorListener = this
        addTextDialog.show(childFragmentManager, ADD_TEXT_DIALOG_TAG)
    }

    private fun setupVideo() {
        addTextVideo.apply {
            setVideoURI(mainViewModel.selectedVideoUri.value)
            setOnPreparedListener {
                it.start()
            }
        }
    }

    override fun onDone(text: String, colorCode: Int) {
        Toast.makeText(context, "$text, $colorCode", Toast.LENGTH_SHORT).show()
    }

}
