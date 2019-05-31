package com.shykun.volodymyr.videoeditor.usecase

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.shykun.volodymyr.ffmpeglib.ContentType
import com.shykun.volodymyr.ffmpeglib.ffmpeg.FFMpegCallback
import com.shykun.volodymyr.ffmpeglib.ffmpeg.audio.FFmpegAudioExtractor
import com.shykun.volodymyr.ffmpeglib.getOutputPath
import com.shykun.volodymyr.videoeditor.getProgressDialog
import java.io.File


class ExtractAudioUseCase(private val videoUri: Uri, private val context: Context) {

    val progressDialog = getProgressDialog(context)

    fun execute() {

        FFmpegAudioExtractor(context)
            .setVideoUri(videoUri)
            .setOutputPath(getOutputPath() + "audio")
            .setOutputFileName("audio_" + System.currentTimeMillis() + ".mp3")
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
                    val file = File(resultPath)
                    val intent = Intent(Intent.ACTION_VIEW)
                    val apkURI = FileProvider.getUriForFile(
                        context,
                        context.applicationContext
                            .packageName + ".provider", file
                    )
                    intent.setDataAndType(apkURI, "audio/mp3")
                    context.startActivity(intent)
                }
            })
            .execute()
    }
}