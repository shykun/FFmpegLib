package com.shykun.volodymyr.videoeditor.usecase

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler
import com.shykun.volodymyr.ffmpeglib.FFmpegExecutor
import com.shykun.volodymyr.ffmpeglib.getPath
import com.shykun.volodymyr.ffmpeglib.getSavePath
import com.shykun.volodymyr.videoeditor.getProgressDialog

class CutUseCase(private val ffmpeg: FFmpegExecutor, private val context: Context) {

    val progressDialog = getProgressDialog(context)

    fun execute(startMs: Int, endMs: Int) {

        val yourRealPath = getPath(context, ffmpeg.videoUri)
        val filePath = getSavePath("cut_video")

        ffmpeg.executeCutVideoCommand(startMs, endMs, yourRealPath!!, filePath,
            object : ExecuteBinaryResponseHandler() {
                override fun onFinish() {
                    progressDialog.dismiss()
                }

                override fun onSuccess(message: String?) {
                    Toast.makeText(context, "SUCCESS", Toast.LENGTH_SHORT).show()
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(filePath))
                    intent.setDataAndType(Uri.parse(filePath), "video/mp4")
                    context.startActivity(intent)
                }

                override fun onFailure(message: String?) {
                    Toast.makeText(context, "FAILURE", Toast.LENGTH_SHORT).show()
                }

                override fun onProgress(message: String?) {
                    progressDialog.setMessage("progress : $message")
                }

                override fun onStart() {
                    progressDialog.setMessage("Processing...")
                    progressDialog.show()
                }
            })
    }
}