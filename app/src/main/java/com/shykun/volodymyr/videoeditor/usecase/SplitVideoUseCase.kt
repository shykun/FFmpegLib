package com.shykun.volodymyr.videoeditor.usecase

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.shykun.volodymyr.ffmpeglib.ContentType
import com.shykun.volodymyr.ffmpeglib.ffmpeg.FFMpegCallback
import com.shykun.volodymyr.ffmpeglib.ffmpeg.video.FFmpegVideoSlowMotion
import com.shykun.volodymyr.ffmpeglib.ffmpeg.video.FFmpegVideoSplitter
import com.shykun.volodymyr.ffmpeglib.getOutputPath
import com.shykun.volodymyr.videoeditor.getProgressDialog
import java.io.File

class SplitVideoUseCase(private val videoUri: Uri, context: Context) : BaseUseCase(context) {

    fun execute(segmentTimeInSec: Int) {
        FFmpegVideoSplitter(context, videoUri, object : FFMpegCallback {
            override fun onStart() {
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

            override fun onFinish() {
                progressDialog.dismiss()
            }
        })
            .setSegmentTime(segmentTimeInSec)
            .setOutputPath(getOutputPath() + "video")
            .setOutputFileName("splitted_video" + System.currentTimeMillis() + ".mp4")
            .execute()
    }
}