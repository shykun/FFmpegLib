package com.shykun.volodymyr.videoeditor.usecase

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.shykun.volodymyr.ffmpeglib.*
import com.shykun.volodymyr.ffmpeglib.ffmpeg.FFMpegCallback
import com.shykun.volodymyr.ffmpeglib.ffmpeg.video.FFmpegVideoTrimmer
import com.shykun.volodymyr.videoeditor.getProgressDialog
import java.io.File

class CutUseCase(
    private val context: Context,
    private val videoUri: Uri,
    private val callback: FFMpegCallback,
    private val startMs: Int,
    private val endMs: Int
) : BaseUseCase {

    override fun execute() {
        FFmpegVideoTrimmer(context, videoUri, callback)
            .setStartTime(startMs)
            .setEndTime(endMs)
            .setOutputPath(getOutputPath() + "video")
            .setOutputFileName("trimmed_" + System.currentTimeMillis() + ".mp4")
            .execute()
    }
}