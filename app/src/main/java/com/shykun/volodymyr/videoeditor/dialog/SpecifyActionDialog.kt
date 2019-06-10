package com.shykun.volodymyr.videoeditor.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.shykun.volodymyr.videoeditor.R
import kotlinx.android.synthetic.main.dialog_specify_action.*

const val SPECIFY_DIALOG_TITLE_KEY = "specify_dialog_title_key"
const val SPECIFY_DIALOG_HINT_KEY = "specify_dialog_hint_key"


class SpecifyActionDialog : DialogFragment() {

    lateinit var specifyDialogClickListener: SpecifyDialogClickListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val title = arguments?.getString(SPECIFY_DIALOG_TITLE_KEY)!!
        val hint = arguments?.getString(SPECIFY_DIALOG_HINT_KEY, "")
        val view = LayoutInflater.from(this.context).inflate(R.layout.dialog_specify_action, null)
        val input = view.findViewById<TextInputEditText>(R.id.specifyDialogInput)

        view.findViewById<TextInputLayout>(R.id.specifyDialogTIL).hint = hint

        return AlertDialog.Builder(activity!!)
            .setTitle(title)
            .setView(view)
            .setPositiveButton("Confirm") { _, _ -> specifyDialogClickListener.onConfirmClicked(input.text.toString()) }
            .setNegativeButton("Cancel") { _, _ ->  }
            .create()

    }

    companion object {
        fun newInstance(title: String, hint: String? = null): SpecifyActionDialog {
            val dialog = SpecifyActionDialog()
            val args = Bundle()

            args.putString(SPECIFY_DIALOG_TITLE_KEY, title)
            args.putString(SPECIFY_DIALOG_HINT_KEY, hint)
            dialog.arguments = args

            return dialog
        }
    }
}


interface SpecifyDialogClickListener {
    fun onConfirmClicked(input: String)
}