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

class FFmpegVideoSlowMotion(private val context: Context) {

    private var videoUri: Uri? = null
    private var callback: FFMpegCallback? = null
    private var outputPath = ""
    private var outputFileName = ""
    private var coefficient = 1.0

    fun setVideoUri(videoUri: Uri): FFmpegVideoSlowMotion {
        this.videoUri = videoUri
        return this
    }

    fun setCallback(callback: FFMpegCallback): FFmpegVideoSlowMotion {
        this.callback = callback
        return this
    }

    fun setOutputPath(output: String): FFmpegVideoSlowMotion {
        this.outputPath = output
        return this
    }

    fun setOutputFileName(output: String): FFmpegVideoSlowMotion {
        this.outputFileName = output
        return this
    }

    fun setCoefficient(coefficient: Double) {
        this.coefficient = coefficient
    }

    fun execute() {
        val outputLocation = getConvertedFile(outputPath, outputFileName)
        val path = getPath(context, videoUri!!)

        val command = arrayOf(
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

        try {
            FFmpeg.getInstance(context).execute(command, object : ExecuteBinaryResponseHandler() {
                override fun onStart() {
                    callback?.onStart()
                }

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