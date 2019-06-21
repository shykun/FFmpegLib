package com.shykun.volodymyr.videoeditor.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.shykun.volodymyr.videoeditor.R

const val  ADD_TEXT_DIALOG_TEXT_KEY = "add_text_dialog_text_key"
const val ADD_TEXT_DIALOG_TEXT_COLOR_KEY = "add_text_dialog_text_color_key"

class AddTextDialog : DialogFragment() {

    lateinit var doneButton: TextView
    lateinit var textInput: EditText
    lateinit var textColorPicker: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_add_text, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        doneButton = view.findViewById(R.id.addTextDoneButton)
        textInput = view.findViewById(R.id.addTextInput)
        textColorPicker = view.findViewById(R.id.addTextColorPickerRecyclerView)
    }

    companion object {
        fun newInstance(text: String = "", colorCode: Int = android.R.color.white): AddTextDialog {
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