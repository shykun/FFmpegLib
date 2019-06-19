package com.shykun.volodymyr.videoeditor.usecase

import android.app.AlertDialog
import android.content.Context
import com.shykun.volodymyr.videoeditor.getProgressDialog

open class BaseUseCase(val context: Context) {
    val progressDialog: AlertDialog = getProgressDialog(context)
}