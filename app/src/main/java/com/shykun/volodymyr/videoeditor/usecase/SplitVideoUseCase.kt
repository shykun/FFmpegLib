package com.shykun.volodymyr.videoeditor.usecase

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.shykun.volodymyr.ffmpeglib.ContentType
import com.shykun.volodymyr.ffmpeglib.ffmpeg.FFMpegCallback
import com.shykun.volodymyr.ffmpeglib.ffmpeg.video.FFmpegVideoSplitter
import com.shykun.volodymyr.ffmpeglib.getOutputPath
import java.io.File

class SplitVideoUseCase(
    private val context: Context,
    private val videoUri: Uri,
    private val callback: FFMpegCallback,
    private val segmentTimeInSec: Int
) : BaseUseCase {

    override fun execute() {
        FFmpegVideoSplitter(context, videoUri, callback)
            .setSegmentTime(segmentTimeInSec)
            .setOutputPath(getOutputPath() + "video")
            .setOutputFileName("splitted_video" + System.currentTimeMillis() + ".mp4")
            .execute()
    }
}