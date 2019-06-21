package com.shykun.volodymyr.videoeditor.usecase

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.shykun.volodymyr.ffmpeglib.*
import com.shykun.volodymyr.ffmpeglib.ffmpeg.FFMpegCallback
import com.shykun.volodymyr.ffmpeglib.ffmpeg.video.FFmpegVideoSlowMotionOriginal
import java.io.File

class SlowMotionOriginalUseCase(
    private val context: Context,
    private val videoUri: Uri,
    private val callback: FFMpegCallback
) : BaseUseCase {

    override fun execute() {
        FFmpegVideoSlowMotionOriginal(context, videoUri, callback)
            .setOutputPath(getOutputPath() + "video")
            .setOutputFileName("slowmotion" + System.currentTimeMillis() + ".mp4")
            .execute()
    }
}