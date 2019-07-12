package com.shykun.volodymyr.ffmpeglib.ffmpeg

import android.content.Context
import android.net.Uri
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException
import com.shykun.volodymyr.ffmpeglib.ContentType
import com.shykun.volodymyr.ffmpeglib.getConvertedFile
import java.io.File
import java.io.IOException

abstract class FFmpegBase(
    protected val context: Context,
    protected val videoUri: Uri?,
    protected val callback: FFMpegCallback?
) {
    protected var outputPath = ""
    protected var outputFileName = ""


    abstract fun getCommand(): Array<String?>

    abstract fun getContentType(): ContentType

    fun setOutputPath(output: String): FFmpegBase {
        this.outputPath = output
        return this
    }

    fun setOutputFileName(output: String): FFmpegBase {
        this.outputFileName = output
        return this
    }

    protected open fun getOutputLocation(): File {
        return getConvertedFile(outputPath, outputFileName)
    }

    fun execute() {
        val cmd = getCommand()
        val outputLocation = getOutputLocation()

        try {
            FFmpeg.getInstance(context).execute(cmd, object : ExecuteBinaryResponseHandler() {
                override fun onStart() {
                    callback?.onStartProcessing()
                }

                override fun onProgress(message: String?) {
                    callback?.onProgress(message!!)
                }

                override fun onSuccess(message: String?) {
                    callback?.onSuccess(outputLocation, getContentType())
                }

                override fun onFailure(message: String?) {
                    if (outputLocation.exists()) {
                        outputLocation.delete()
                    }
                    callback?.onFailure(IOException(message))
                }

                override fun onFinish() {
                    callback?.onFinishProcessing()
                }
            })
        } catch (e: Exception) {
            callback?.onFailure(e)
        } catch (e2: FFmpegCommandAlreadyRunningException) {
            callback?.onNotAvailable(e2)
        }
    }
}