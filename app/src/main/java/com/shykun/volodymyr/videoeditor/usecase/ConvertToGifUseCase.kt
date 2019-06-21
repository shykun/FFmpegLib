package com.shykun.volodymyr.videoeditor.usecase

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.shykun.volodymyr.ffmpeglib.ContentType
import com.shykun.volodymyr.ffmpeglib.ffmpeg.FFMpegCallback
import com.shykun.volodymyr.ffmpeglib.ffmpeg.image.FFmpegVideoToGif
import com.shykun.volodymyr.ffmpeglib.getOutputPath
import java.io.File

class ConvertToGifUseCase(
    private val context: Context,
    private val videoUri: Uri,
    private val callback: FFMpegCallback
) : BaseUseCase {

    override fun execute() {
        FFmpegVideoToGif(context, videoUri, callback/*object : FFMpegCallback {
            override fun onStartProcessing() {
                progressDialog.show()
            }

            override fun onProgress(progress: String) {
                progressDialog.setMessage("progress : $progress")
            }

            override fun onSuccess(convertedFile: File, contentType: ContentType) {
                Toast.makeText(context, "SUCCESS", Toast.LENGTH_SHORT).show()
                val intent = Intent(Intent.ACTION_VIEW)
                val apkURI = FileProvider.getUriForFile(
                    context,
                    context.applicationContext
                        .packageName + ".provider", convertedFile
                )
                intent.setDataAndType(apkURI, "video/mp4")
                context.startActivity(intent)
            }

            override fun onFailure(error: Exception) {
                Toast.makeText(context, "FAILURE", Toast.LENGTH_SHORT).show()
            }

            override fun onNotAvailable(error: Exception) {
                Toast.makeText(context, "NOT AVAILABLE", Toast.LENGTH_SHORT).show()
            }

            override fun onFinishProcessing() {
                progressDialog.dismiss()
            }
        }*/)
            .setFPS(30)
            .setScale(500)
            .setOutputPath(getOutputPath() + "video")
            .setOutputFileName("gif_" + System.currentTimeMillis() + ".mp4")
            .execute()

    }
}