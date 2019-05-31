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
import java.io.File
import java.io.IOException

class FFmpegVideoToImages(private val context: Context) {

    private var videoUri: Uri? = null
    private var callback: FFMpegCallback? = null
    private var outputPath = ""
    private var outputFileName = ""
    private var interval = 1.0

    fun setVideoUri(videoUri: Uri): FFmpegVideoToImages {
        this.videoUri = videoUri
        return this
    }

    fun setCallback(callback: FFMpegCallback): FFmpegVideoToImages {
        this.callback = callback
        return this
    }

    fun setOutputPath(output: String): FFmpegVideoToImages {
        this.outputPath = output
        return this
    }

    fun setOutputFileName(output: String): FFmpegVideoToImages {
        this.outputFileName = output
        return this
    }

    fun setInterval(interval: Double): FFmpegVideoToImages {
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
