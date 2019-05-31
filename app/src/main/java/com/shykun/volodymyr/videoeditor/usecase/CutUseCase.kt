package com.shykun.volodymyr.videoeditor.usecase

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.shykun.volodymyr.ffmpeglib.*
import com.shykun.volodymyr.ffmpeglib.ffmpeg.FFMpegCallback
import com.shykun.volodymyr.ffmpeglib.ffmpeg.video.FFmpegVideoTrimmer
import com.shykun.volodymyr.videoeditor.getProgressDialog
import java.io.File

class CutUseCase(private val videoUri: Uri, private val context: Context) {

    val progressDialog = getProgressDialog(context)

    fun execute(startMs: Int, endMs: Int) {
        FFmpegVideoTrimmer(context)
            .setVideoUri(videoUri)
            .setStartTime(startMs)
            .setEndTime(endMs)
            .setOutputPath(getOutputPath() + "video")
            .setOutputFileName("trimmed_" + System.currentTimeMillis() + ".mp4")
            .setCallback(object : FFMpegCallback {
                override fun onStart() {
                    progressDialog.show()
                }

                override fun onProgress(progress: String) {
                    progressDialog.setMessage("progress : $progress")
                }

                override fun onSuccess(convertedFile: File, contentType: ContentType) {
                    Toast.makeText(context, "SUCCESS", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(error: Exception) {
                    Toast.makeText(context, "FAILURE", Toast.LENGTH_SHORT).show()
                }

                override fun onNotAvailable(error: Exception) {
                    Toast.makeText(context, "NOT AVAILABLE", Toast.LENGTH_SHORT).show()
                }

                override fun onFinish(resultPath: String) {
                    progressDialog.dismiss()
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(resultPath))
                    intent.setDataAndType(Uri.parse(resultPath), "video/mp4")
                    context.startActivity(intent)
                }
            })
            .execute()
    }
}