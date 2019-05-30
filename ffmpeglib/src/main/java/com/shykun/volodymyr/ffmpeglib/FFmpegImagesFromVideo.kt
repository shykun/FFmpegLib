package com.shykun.volodymyr.ffmpeglib

import android.content.Context
import android.net.Uri
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException
import java.io.File
import java.io.IOException

class FFmpegImagesFromVideo(private val context: Context) {

    private var videoUri: Uri? = null
    private var callback: FFMpegCallback? = null
    private var outputPath = ""
    private var outputFileName = ""
    private var interval = 1.0

    fun setVideoUri(videoUri: Uri): FFmpegImagesFromVideo {
        this.videoUri = videoUri
        return this
    }

    fun setCallback(callback: FFMpegCallback): FFmpegImagesFromVideo {
        this.callback = callback
        return this
    }

    fun setOutputPath(output: String): FFmpegImagesFromVideo {
        this.outputPath = output
        return this
    }

    fun setOutputFileName(output: String): FFmpegImagesFromVideo {
        this.outputFileName = output
        return this
    }

    fun setInterval(interval: Double): FFmpegImagesFromVideo {
        this.interval = interval
        return this
    }

    fun extract() {
        val inputLocation = getPath(context, videoUri!!)
        val outputLocation = getConvertedFile(outputPath, "")

        val cmd = arrayOf(
            "-i",
            inputLocation!!,
            "-r",
            interval.toString(),
            outputLocation.path + File.separator + outputFileName + "_%04d.jpg"
        )

        try {
            FFmpeg.getInstance(context).execute(cmd, object : ExecuteBinaryResponseHandler() {
                override fun onStart() {
                    callback?.onStart()
                }

                override fun onProgress(message: String?) {
                    callback?.onProgress(message!!)
                }

                override fun onSuccess(message: String?) {
                    callback?.onSuccess(outputLocation, ContentType.IMAGES)
                }

                override fun onFailure(message: String?) {
                    if (outputLocation.exists()) {
                        outputLocation.delete()
                    }
                    callback?.onFailure(IOException(message))
                }

                override fun onFinish() {
                    callback?.onFinish(outputPath)
                }
            })
        } catch (e: Exception) {
            callback?.onFailure(e)
        } catch (e2: FFmpegCommandAlreadyRunningException) {
            callback?.onNotAvailable(e2)
        }
    }
}
