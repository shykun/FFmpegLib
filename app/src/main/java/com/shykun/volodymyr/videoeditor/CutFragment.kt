package com.shykun.volodymyr.videoeditor

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.FileProvider
import com.shykun.volodymyr.ffmpeglib.ContentType
import com.shykun.volodymyr.ffmpeglib.ffmpeg.FFMpegCallback
import com.shykun.volodymyr.videoeditor.usecase.CutUseCase
import java.io.File

class CutFragment : RangeSeekBarFragment(), FFMpegCallback {
    private lateinit var progressDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = getProgressDialog(context!!)
    }
    override fun onStartProcessing() {
        progressDialog.show()
    }

    override fun onProgress(progress: String) {
        progressDialog.setMessage("progress : $progress")
    }

    override fun onSuccess(convertedFile: File, contentType: ContentType) {
        Toast.makeText(context, "SUCCESS", Toast.LENGTH_SHORT).show()
        val intent = Intent(Intent.ACTION_VIEW)
        val apkURI = context?.let {
            FileProvider.getUriForFile(
                it,
                it.applicationContext
                    .packageName + ".provider", convertedFile
            )
        }
        intent.setDataAndType(apkURI, "video/mp4")
        context?.startActivity(intent)
    }

    override fun onFailure(error: Exception) {
        Toast.makeText(context, "FAILURE", Toast.LENGTH_SHORT).show()
    }

    override fun onNotAvailable(error: Exception) {
        Toast.makeText(context, "NOT AVAILABLE", Toast.LENGTH_SHORT).show()
    }

    override fun onFinishProcessing() {
        progressDialog.hide()
    }

    override val confirmAction = {
        CutUseCase(context!!, mainViewModel.selectedVideoUri.value!!, this, curStartValue, curEndValue).execute()
    }
}