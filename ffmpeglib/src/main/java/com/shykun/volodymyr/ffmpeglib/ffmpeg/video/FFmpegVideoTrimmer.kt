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
import com.shykun.volodymyr.ffmpeglib.refreshGallery
import java.io.IOException

//Trim video

class FFmpegVideoTrimmer(context: Context, videoUri: Uri, callback: FFMpegCallback) : FFmpegBase(context, videoUri, callback) {

    private var startTime = 0
    private var endTime = 0

    fun setStartTime(startTimeMills: Int): FFmpegVideoTrimmer {
        this.startTime = startTimeMills
        return this
    }

    fun setEndTime(endTimeMills: Int): FFmpegVideoTrimmer {
        this.endTime = endTimeMills
        return this
    }

    override fun getCommand(): Array<String?> {
        val outputLocation = getOutputLocation()
        val path = getPath(context, videoUri)

        return arrayOf(
            "-i",
            path,
            "-ss",
            (startTime / 1000).toString(),
            "-t",
            ((endTime - startTime) / 1000).toString(),
            "-c",
            "copy",
            outputLocation.path
        )
    }

    override fun getContentType(): ContentType = ContentType.VIDEO
}