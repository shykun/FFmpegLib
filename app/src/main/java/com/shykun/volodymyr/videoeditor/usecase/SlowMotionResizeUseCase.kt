package com.shykun.volodymyr.videoeditor.usecase

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.shykun.volodymyr.ffmpeglib.ContentType
import com.shykun.volodymyr.ffmpeglib.ffmpeg.FFMpegCallback
import com.shykun.volodymyr.ffmpeglib.ffmpeg.video.FFmpegVideoSlowMotionResize
import com.shykun.volodymyr.ffmpeglib.getOutputPath
import java.io.File

class SlowMotionResizeUseCase(
    private val context: Context,
    private val videoUri: Uri,
    private val callback: FFMpegCallback
) : BaseUseCase {
    override fun execute() {
        FFmpegVideoSlowMotionResize(context, videoUri, callback)
            .setOutputPath(getOutputPath() + "video")
            .setOutputFileName("slowmotion" + System.currentTimeMillis() + ".mp4")
            .execute()
    }
}