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

class ExtractImagesUseCase(
    private val context: Context,
    private val videoUri: Uri,
    private val callback: FFMpegCallback,
    private val interval: Double
) : BaseUseCase {

    override fun execute() {
        FFmpegVideoToImages(context, videoUri, callback)
            .setInterval(interval)
            .setOutputPath(getOutputPath() + "images")
            .setOutputFileName("images")
            .execute()
    }
}