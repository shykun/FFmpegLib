package com.shykun.volodymyr.ffmpeglib.ffmpeg.video

import android.content.Context
import android.net.Uri
import com.shykun.volodymyr.ffmpeglib.ffmpeg.FFMpegCallback
import com.shykun.volodymyr.ffmpeglib.getPath

//Get fastmotion video with smaller resolution

class FFmpegVideoFastMotionResize(context: Context, videoUri: Uri, callback: FFMpegCallback) : FFmpegVideoFastMotionOriginal(context, videoUri, callback) {
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
}