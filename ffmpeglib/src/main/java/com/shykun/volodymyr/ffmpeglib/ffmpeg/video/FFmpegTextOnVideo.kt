package com.shykun.volodymyr.ffmpeglib.ffmpeg.video

import android.content.Context
import android.net.Uri
import com.shykun.volodymyr.ffmpeglib.ContentType
import com.shykun.volodymyr.ffmpeglib.ffmpeg.FFMpegCallback
import com.shykun.volodymyr.ffmpeglib.ffmpeg.FFmpegBase
import com.shykun.volodymyr.ffmpeglib.getPath
import java.io.File

//Add text to video

class FFmpegTextOnVideo(context: Context, videoUri: Uri, callback: FFMpegCallback) :
    FFmpegBase(context, videoUri, callback) {

    private var font: File? = null
    private var texts: List<AddedText?>? = null

    fun setTexts(texts: List<AddedText?>): FFmpegTextOnVideo {
        this.texts = texts
        return this
    }

    fun setFont(output: File): FFmpegTextOnVideo {
        this.font = output
        return this
    }

    override fun getCommand(): Array<String?> {
        val outputLocation = getOutputLocation()
        val path = getPath(context, videoUri)

        return arrayOf(
            "-i",
            path,
            "-vf",
            getConcatenatedCommand(),
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

    private fun getConcatenatedCommand(): String {
        val sb = StringBuilder()

        texts?.forEachIndexed { i, it ->
            if (it != null)
                sb.append(
                    "drawtext=fontfile=" + font!!.path +
                            ":text=" + it.text +
                            ": fontcolor=" + it.colorCodeString +
                            ": fontsize=" + it.textSize +
                            ": " + it.position +
                            if (i < texts!!.size - 1) ", " else ""
                )
        }

        return sb.toString()
    }

    companion object {
        var POSITION_BOTTOM_RIGHT = "x=w-tw-10:y=h-th-10"
        var POSITION_TOP_RIGHT = "x=w-tw-10:y=10"
        var POSITION_TOP_LEFT = "x=10:y=10"
        var POSITION_BOTTOM_LEFT = "x=10:h-th-10"
        var POSITION_CENTER_BOTTOM = "x=(main_w/2-text_w/2):y=main_h-(text_h*2)"
        var POSITION_CENTER_ALLIGN = "x=(w-text_w)/2: y=(h-text_h)/3"
    }
}

data class AddedText(
    var text: String,
    var colorCode: Int,
    var textSize: Int,
    var x: Int,
    var y: Int
) {
    val colorCodeString: String
        get() = String.format("#%06X", 0xFFFFFF and colorCode)

    val position: String
        get() = "x=$x:y=$y"
}
