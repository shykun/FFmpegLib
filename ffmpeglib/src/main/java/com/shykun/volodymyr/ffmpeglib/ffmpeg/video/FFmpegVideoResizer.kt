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
import java.io.IOException

class FFmpegVideoResizer(private val context: Context) {

    private var videoUri: Uri? = null
    private var callback: FFMpegCallback? = null
    private var outputPath = ""
    private var outputFileName = ""
    private var size = ""

    fun setVideoUri(videoUri: Uri): FFmpegVideoResizer {
        this.videoUri = videoUri
        return this
    }

    fun setCallback(callback: FFMpegCallback): FFmpegVideoResizer {
        this.callback = callback
        return this
    }

    fun setOutputPath(output: String): FFmpegVideoResizer {
        this.outputPath = output
        return this
    }

    fun setOutputFileName(output: String): FFmpegVideoResizer {
        this.outputFileName = output
        return this
    }

    fun setSize(output: String): FFmpegVideoResizer {
        this.size = output
        return this
    }

    fun execute() {
        val outputLocation = getConvertedFile(outputPath, outputFileName)
        val path = getPath(context, videoUri!!)

        val cmd = arrayOf("-i", path, "-vf", "scale=" + size, outputLocation.path, "-hide_banner")

        try {
            FFmpeg.getInstance(context).execute(cmd, object : ExecuteBinaryResponseHandler() {
                override fun onStart() {}

                override fun onProgress(message: String?) {
                    callback?.onProgress(message!!)
                }

                override fun onSuccess(message: String?) {
                    callback?.onSuccess(outputLocation, ContentType.VIDEO)

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