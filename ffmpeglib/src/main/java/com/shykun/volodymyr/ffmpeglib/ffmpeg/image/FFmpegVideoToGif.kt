package com.shykun.volodymyr.ffmpeglib.ffmpeg.image

import android.content.Context
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException
import com.shykun.volodymyr.ffmpeglib.ContentType
import com.shykun.volodymyr.ffmpeglib.ffmpeg.FFmpegBase
import com.shykun.volodymyr.ffmpeglib.getConvertedFile
import com.shykun.volodymyr.ffmpeglib.getPath
import java.io.IOException

class FFmpegVideoToGif(context: Context) : FFmpegBase(context) {
    override fun getCommand(): Array<String?> {
        val path = getPath(context, videoUri!!)

        return arrayOf(
            "-i",
            path,
            "-r",
            "$fps",
            "-ss",
            1.toString(),
            "-t",
            5.toString(),
            "-vf",
            "scale=$scale",
            "agif_r${fps}_d20_$scale.gif",
            "-hide_banner"
        )
    }

    override fun getContentType(): ContentType = ContentType.GIF

    private var duration = 0
    private var fps = 0
    private var scale = 0


    fun setDuration(output: Int): FFmpegVideoToGif {
        this.duration = output
        return this
    }

    fun setFPS(output: Int): FFmpegVideoToGif {
        this.fps = output
        return this
    }

    fun setScale(output: Int): FFmpegVideoToGif {
        this.scale = output
        return this
    }

}
