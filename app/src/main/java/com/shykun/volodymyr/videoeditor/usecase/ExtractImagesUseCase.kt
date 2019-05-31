package com.shykun.volodymyr.videoeditor.usecase

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.shykun.volodymyr.ffmpeglib.*
import com.shykun.volodymyr.ffmpeglib.ffmpeg.FFMpegCallback
import com.shykun.volodymyr.ffmpeglib.ffmpeg.image.FFmpegVideoToImages
import com.shykun.volodymyr.videoeditor.getProgressDialog
import java.io.File

class ExtractImagesUseCase(private val videoUri: Uri, private val context: Context) {

    val progressDialog = getProgressDialog(context)

    fun execute(interval: Double) {
        FFmpegVideoToImages(context)
            .setVideoUri(videoUri)
            .setOutputPath(getOutputPath() + "images")
            .setOutputFileName("images")
            .setInterval(interval)
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
                    intent.setDataAndType(apkURI, "image/jpg")
                    context.startActivity(intent)
                }
            })
            .extract()



//        val filePrefix = "extract_picture"
//        val fileExtn = ".jpg"
//        val yourRealPath = getPath(context, ffmpeg.videoUri)
//
//        val dir = getImagesSaveDir("VideoEditor")
//        val filePath = dir.absolutePath
//        val dest = File(dir, "$filePrefix%03d$fileExtn")
//        dir.mkdir()
//
//        ffmpeg.extractImagesVideo(startMs, endMs, yourRealPath!!, dest.absolutePath,
//            object : ExecuteBinaryResponseHandler() {
//                override fun onFinish() {
//                    progressDialog.dismiss()
//                }
//
//                override fun onSuccess(message: String?) {
//                    Toast.makeText(context, "SUCCESS", Toast.LENGTH_SHORT).show()
////                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(filePath))
////                    intent.setDataAndType(Uri.parse(filePath), "video/mp4")
////                    context.startActivity(intent)
//                }
//
//                override fun onFailure(message: String?) {
//                    Toast.makeText(context, "FAILURE", Toast.LENGTH_SHORT).show()
//                }
//
//                override fun onProgress(message: String?) {
//                    progressDialog.setMessage("progress : $message")
//                }
//
//                override fun onStart() {
//                    progressDialog.setMessage("Processing...")
//                    progressDialog.show()
//                }
//            })
    }
}