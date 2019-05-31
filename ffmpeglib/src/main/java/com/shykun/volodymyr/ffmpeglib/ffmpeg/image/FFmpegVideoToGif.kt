package com.shykun.volodymyr.ffmpeglib.ffmpeg.image

import android.content.Context
import android.net.Uri
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException
import com.shykun.volodymyr.ffmpeglib.ContentType
import com.shykun.volodymyr.ffmpeglib.ffmpeg.FFMpegCallback
import com.shykun.volodymyr.ffmpeglib.getConvertedFile
import com.shykun.volodymyr.ffmpeglib.getPath
import java.io.IOException

class FFmpegVideoToGif private constructor(private val context: Context) {

    private var videoUri: Uri? = null
    private var callback: FFMpegCallback? = null
    private var outputPath = ""
    private var outputFileName = ""
    private var duration = ""
    private var fps = ""
    private var scale = ""

    fun setVideoUri(videoUri: Uri): FFmpegVideoToGif {
        this.videoUri = videoUri
        return this
    }

    fun setCallback(callback: FFMpegCallback): FFmpegVideoToGif {
        this.callback = callback
        return this
    }

    fun setOutputPath(output: String): FFmpegVideoToGif {
        this.outputPath = output
        return this
    }

    fun setOutputFileName(output: String): FFmpegVideoToGif {
        this.outputFileName = output
        return this
    }

    fun setDuration(output: String): FFmpegVideoToGif {
        this.duration = output
        return this
    }

    fun setFPS(output: String): FFmpegVideoToGif {
        this.fps = output
        return this
    }

    fun setScale(output: String): FFmpegVideoToGif {
        this.scale = output
        return this
    }

    fun create() {
        val outputLocation = getConvertedFile(outputPath, outputFileName)
        val path = getPath(context, videoUri!!)

        val cmd = arrayOf(
            "-i",
            path,
            "-vf",
            "scale=" + scale + ":-1", "-t",
            duration,
            "-r",
            fps,
            outputLocation.path
        )

        try {
            FFmpeg.getInstance(context).execute(cmd, object : ExecuteBinaryResponseHandler() {
                override fun onStart() {}

                override fun onProgress(message: String?) {
                    callback?.onProgress(message!!)
                }

                override fun onSuccess(message: String?) {
                    callback?.onSuccess(outputLocation, ContentType.GIF)

                }

                override fun onFailure(message: String?) {
                    if (outputLocation.exists()) {
                        outputLocation.delete()
                    }
                    callback?.onFailure(IOException(message))
                }

                override fun onFinish() {
                    callback?.onFinish()
                }
            })
        } catch (e: Exception) {
            callback?.onFailure(e)
        } catch (e2: FFmpegCommandAlreadyRunningException) {
            callback?.onNotAvailable(e2)
        }

    }
}