package com.shykun.volodymyr.videoeditor.dialog

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.shykun.volodymyr.videoeditor.R
import com.shykun.volodymyr.videoeditor.adapter.ColorPickerAdapter
import com.shykun.volodymyr.videoeditor.adapter.OnColorPickerClickListener
import kotlinx.android.synthetic.main.dialog_add_text.*


const val ADD_TEXT_DIALOG_TEXT_KEY = "add_text_dialog_text_key"
const val ADD_TEXT_DIALOG_TEXT_COLOR_KEY = "add_text_dialog_text_color_key"

interface OnTextEditorListener {
    fun onDone(inputText: String, colorCode: Int)
}

class AddTextDialog : DialogFragment(), OnColorPickerClickListener {

    lateinit var inputMethodManager: InputMethodManager
    lateinit var colorPickerAdapter: ColorPickerAdapter

    var onTextEditorListener: OnTextEditorListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_add_text, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupColorPickerList()
        setupTextInput()
        setupDoneButton(view)
    }

    private fun setupDoneButton(view: View) {
        addTextDoneButton.setOnClickListener {
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            val text = addTextInput.text.toString()
            val color = addTextInput.currentTextColor

            if (text.isNotEmpty())
                onTextEditorListener?.onDone(text, color)
            dismiss()
        }
    }

    private fun setupTextInput() {
        addTextInput.requestFocus()
        inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

        addTextInput.apply {
            setText(arguments?.getString(ADD_TEXT_DIALOG_TEXT_KEY, ""))
            setTextColor(ContextCompat.getColor(activity!!, R.color.white_color_picker))
        }
    }

    private fun setupColorPickerList() {
        colorPickerAdapter = ColorPickerAdapter(activity!!)
        addTextColorPickerRecyclerView.apply {
            adapter = colorPickerAdapter
            layoutManager = LinearLayoutManager(activity!!, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
        colorPickerAdapter.onColorPickerListener = this
    }

    override fun onStart() {
        super.onStart()

        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.apply {
            setLayout(width, height)
            setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

    }

    override fun onColorPicked(colorCode: Int) {
        addTextInput.setTextColor(colorCode)
    }

    companion object {
        fun newInstance(text: String = "", colorCode: Int = R.color.white_color_picker): AddTextDialog {
            val args = Bundle()
            val dialog = AddTextDialog()

            args.apply {
                putString(ADD_TEXT_DIALOG_TEXT_KEY, text)
                putInt(ADD_TEXT_DIALOG_TEXT_COLOR_KEY, colorCode)
            }
            dialog.arguments = args

            return dialog
        }
    }
}