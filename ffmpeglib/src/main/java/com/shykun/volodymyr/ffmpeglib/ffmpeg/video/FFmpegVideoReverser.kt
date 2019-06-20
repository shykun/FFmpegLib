package com.shykun.volodymyr.ffmpeglib.ffmpeg.video

import android.content.Context
import android.net.Uri
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException
import com.shykun.volodymyr.ffmpeglib.ContentType
import com.shykun.volodymyr.ffmpeglib.ffmpeg.FFMpegCallback
import com.shykun.volodymyr.ffmpeglib.ffmpeg.FFmpegBase
import com.shykun.volodymyr.ffmpeglib.getConvertedFile
import com.shykun.volodymyr.ffmpeglib.getPath
import java.io.IOException

class FFmpegVideoReverser(context: Context) : FFmpegBase(context) {
    override fun getCommand(): Array<String?> {
        val outputLocation = getOutputLocation()
        val path = getPath(context, videoUri!!)

        return arrayOf(
            "-i",
            path,
            "-vf",
            "reverse",
            outputLocation.path
        )
    }

    override fun getContentType(): ContentType = ContentType.VIDEO
}