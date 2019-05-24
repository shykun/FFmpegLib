package com.shykun.volodymyr.videoeditor

import android.app.AlertDialog
import android.content.Context

fun getProgressDialog(context: Context) = AlertDialog.Builder(context)
    .setView(R.layout.dialog_progress)
    .setTitle("Please wait! This may take a moment")
    .setCancelable(false)
    .create()