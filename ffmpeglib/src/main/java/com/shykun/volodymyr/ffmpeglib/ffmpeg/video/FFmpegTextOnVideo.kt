package com.shykun.volodymyr.ffmpeglib.ffmpeg.video

import android.content.Context
import android.net.Uri
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException
import com.shykun.volodymyr.ffmpeglib.ContentType
import com.shykun.volodymyr.ffmpeglib.ffmpeg.FFMpegCallback
import com.shykun.volodymyr.ffmpeglib.ffmpeg.FFmpegBase
import com.shykun.volodymyr.ffmpeglib.getPath
import java.io.File
import java.io.IOException

class FFmpegTextOnVideo(context: Context, videoUri: Uri, callback: FFMpegCallback) : FFmpegBase(context, videoUri, callback) {

    private var font: File? = null
    private var text: String? = null
    private var position: String? = null
    private var color: String? = null
    private var size: String? = null
    private var border: String? = null
    private var addBorder: Boolean? = null

    //Border
    var BORDER_FILLED = ": box=1: boxcolor=black@0.5:boxborderw=5"
    var BORDER_EMPTY = ""


    fun setFont(output: File): FFmpegTextOnVideo {
        this.font = output
        return this
    }

    fun setText(output: String): FFmpegTextOnVideo {
        this.text = output
        return this
    }

    fun setPosition(output: String): FFmpegTextOnVideo {
        this.position = output
        return this
    }

    fun setColor(output: String): FFmpegTextOnVideo {
        this.color = output
        return this
    }

    fun setSize(output: String): FFmpegTextOnVideo {
        this.size = output
        return this
    }

    fun addBorder(output: Boolean): FFmpegTextOnVideo {
        if (output)
            this.border = BORDER_FILLED
        else
            this.border = BORDER_EMPTY
        return this
    }

    override fun getCommand(): Array<String?> {
        val outputLocation = getOutputLocation()
        val path = getPath(context, videoUri)

        return arrayOf(
            "-i",
            path,
            "-vf",
            "drawtext=fontfile=" + font!!.path + ":text=" + text + ": fontcolor=" + color + ": fontsize=" + size + border + ": " + position,
            "-c:v",
            "libx264",
            "-c:a",
            "copy",
            "-movflags",
            "+faststart",
            outputLocation.path
        )
    }

    override fun getContentType(): ContentType = ContentType.VIDEO

    companion object {
        var POSITION_BOTTOM_RIGHT = "x=w-tw-10:y=h-th-10"
        var POSITION_TOP_RIGHT = "x=w-tw-10:y=10"
        var POSITION_TOP_LEFT = "x=10:y=10"
        var POSITION_BOTTOM_LEFT = "x=10:h-th-10"
        var POSITION_CENTER_BOTTOM = "x=(main_w/2-text_w/2):y=main_h-(text_h*2)"
        var POSITION_CENTER_ALLIGN = "x=(w-text_w)/2: y=(h-text_h)/3"
    }
}
