package com.shykun.volodymyr.ffmpeglib.ffmpeg.video

import android.content.Context
import android.net.Uri
import com.shykun.volodymyr.ffmpeglib.ffmpeg.FFMpegCallback
import com.shykun.volodymyr.ffmpeglib.getPath

//Get slowmotion video with smaller resolution

class FFmpegVideoSlowMotionResize(context: Context, videoUri: Uri, callback: FFMpegCallback) : FFmpegVideoSlowMotionOriginal(context, videoUri, callback) {
    override fun getCommand(): Array<String?> {
        val outputLocation = getOutputLocation()
        val path = getPath(context, videoUri)

        return arrayOf(
            "-y",
            "-i",
            path,
            "-filter_complex",
            "[0:v]setpts=$coefficient*PTS[v];[0:a]atempo=0.5[a]",
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
}