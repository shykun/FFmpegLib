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
        FFmpegVideoToGif(context, videoUri, callback)
            .setFPS(30)
            .setScale(500)
            .setOutputPath(getOutputPath() + "video")
            .setOutputFileName("gif_" + System.currentTimeMillis() + ".mp4")
            .execute()

    }
}