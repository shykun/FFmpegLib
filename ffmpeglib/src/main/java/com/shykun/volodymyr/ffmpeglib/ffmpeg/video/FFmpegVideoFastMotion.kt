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

class FFmpegVideoFastMotion(context: Context, videoUri: Uri, callback: FFMpegCallback) : FFmpegBase(context, videoUri, callback) {

    private var coefficient = 0.5

    fun setCoefficient(coefficient: Double) {
        this.coefficient = 1.0 / coefficient
    }

    override fun getCommand(): Array<String?> {
        val outputLocation = getOutputLocation()
        val path = getPath(context, videoUri)

        return arrayOf(
            "-y",
            "-i",
            path,
            "-filter_complex",
            "[0:v]setpts=0.5*PTS[v];[0:a]atempo=2.0[a]",
            "-map",
            "[v]",
            "-map",
            "[a]",
            "-b:v",
            "2097k",
            "-r",
            "60",
            "-vcodec",
            "mpeg4",
            outputLocation.path
        )
    }

    override fun getContentType(): ContentType = ContentType.VIDEO
}