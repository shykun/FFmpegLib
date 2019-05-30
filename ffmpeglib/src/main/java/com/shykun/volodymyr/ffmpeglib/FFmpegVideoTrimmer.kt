package com.shykun.volodymyr.ffmpeglib

import android.content.Context
import android.net.Uri
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException
import java.io.File
import java.io.IOException

class FFmpegVideoTrimmer(private val context: Context) {

    private var videoUri: Uri? = null
    private var callback: FFMpegCallback? = null
    private var outputPath = ""
    private var outputFileName = ""
    private var startTime = 0
    private var endTime = 0

    fun setVideoUri(videoUri: Uri): FFmpegVideoTrimmer {
        this.videoUri = videoUri
        return this
    }

    fun setCallback(callback: FFMpegCallback): FFmpegVideoTrimmer {
        this.callback = callback
        return this
    }

    fun setOutputPath(output: String): FFmpegVideoTrimmer {
        this.outputPath = output
        return this
    }

    fun setOutputFileName(output: String): FFmpegVideoTrimmer {
        this.outputFileName = output
        return this
    }

    fun setStartTime(startTimeMills: Int): FFmpegVideoTrimmer {
        this.startTime = startTimeMills
        return this
    }

    fun setEndTime(endTimeMills: Int): FFmpegVideoTrimmer {
        this.endTime = endTimeMills
        return this
    }

    fun execute() {
        val outputLocation = getConvertedFile(outputPath, outputFileName)
        val path = getPath(context, videoUri!!)

        val cmd = arrayOf(
            "-i",
            path,
            "-ss",
            "" + startTime / 1000,
            "-t",
            "" + (endTime - startTime) / 1000,
            "-c",
            "copy",
            outputLocation.path
        )

        try {
            FFmpeg.getInstance(context).execute(cmd, object : ExecuteBinaryResponseHandler() {
                override fun onStart() {
                    callback!!.onStart()
                }

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
                    callback!!.onFinish(outputLocation.path)
                }
            })
        } catch (e: Exception) {
            callback!!.onFailure(e)
        } catch (e2: FFmpegCommandAlreadyRunningException) {
            callback!!.onNotAvailable(e2)
        }

    }
}