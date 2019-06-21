package com.shykun.volodymyr.videoeditor.usecase

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler
import com.shykun.volodymyr.ffmpeglib.*
import com.shykun.volodymyr.ffmpeglib.ffmpeg.FFMpegCallback
import com.shykun.volodymyr.ffmpeglib.ffmpeg.video.FFmpegVideoReverser
import com.shykun.volodymyr.videoeditor.getProgressDialog
import java.io.File


class ReverseUseCase(
    private val context: Context,
    private val videoUri: Uri,
    private val callback: FFMpegCallback
) : BaseUseCase {

    override fun execute() {
        FFmpegVideoReverser(context, videoUri, callback)
            .setOutputPath(getOutputPath() + "video")
            .setOutputFileName("reversed_" + System.currentTimeMillis() + ".mp4")
            .execute()
    }
}