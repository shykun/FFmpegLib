package com.shykun.volodymyr.ffmpeglib.ffmpeg.video

import android.content.Context
import android.net.Uri
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException
import com.shykun.volodymyr.ffmpeglib.ContentType
import com.shykun.volodymyr.ffmpeglib.ffmpeg.FFMpegCallback
import com.shykun.volodymyr.ffmpeglib.getConvertedFile
import com.shykun.volodymyr.ffmpeglib.getPath
import java.io.File
import java.io.IOException

class FFmpegTextOnVideo(private val context: Context) {

    private var videoUri: Uri? = null
    private var callback: FFMpegCallback? = null
    private var outputPath = ""
    private var outputFileName = ""
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

    fun setVideoUri(videoUri: Uri): FFmpegTextOnVideo {
        this.videoUri = videoUri
        return this
    }

    fun setCallback(callback: FFMpegCallback): FFmpegTextOnVideo {
        this.callback = callback
        return this
    }

    fun setOutputPath(output: String): FFmpegTextOnVideo {
        this.outputPath = output
        return this
    }

    fun setOutputFileName(output: String): FFmpegTextOnVideo {
        this.outputFileName = output
        return this
    }

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

    fun execute() {
        val outputLocation = getConvertedFile(outputPath, outputFileName)
        val path = getPath(context, videoUri!!)

        val cmd = arrayOf(
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

        try {
            FFmpeg.getInstance(context).execute(cmd, object : ExecuteBinaryResponseHandler() {
                override fun onStart() {}

                override fun onProgress(message: String?) {
                    callback!!.onProgress(message!!)
                }

                override fun onSuccess(message: String?) {
                    callback!!.onSuccess(outputLocation, ContentType.VIDEO)

                }

                override fun onFailure(message: String?) {
                    if (outputLocation.exists()) {
                        outputLocation.delete()
                    }
                    callback!!.onFailure(IOException(message))
                }

                override fun onFinish() {
                    callback!!.onFinish()
                }
            })
        } catch (e: Exception) {
            callback!!.onFailure(e)
        } catch (e2: FFmpegCommandAlreadyRunningException) {
            callback!!.onNotAvailable(e2)
        }

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
