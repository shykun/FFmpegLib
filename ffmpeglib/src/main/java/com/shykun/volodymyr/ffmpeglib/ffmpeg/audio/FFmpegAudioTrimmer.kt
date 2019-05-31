package com.shykun.volodymyr.ffmpeglib.ffmpeg.audio

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

class FFmpegAudioTrimmer private constructor(private val context: Context) {

    private var audioUri: Uri? = null
    private var callback: FFMpegCallback? = null
    private var startTime = 0
    private var endTime = 0
    private var outputPath = ""
    private var outputFileName = ""

    fun setAudioUri(audioUri: Uri): FFmpegAudioTrimmer {
        this.audioUri = audioUri
        return this
    }

    fun setCallback(callback: FFMpegCallback): FFmpegAudioTrimmer {
        this.callback = callback
        return this
    }

    fun setStartTime(startTimeMills: Int): FFmpegAudioTrimmer {
        this.startTime = startTimeMills
        return this
    }

    fun setEndTime(endTimeMills: Int): FFmpegAudioTrimmer {
        this.endTime = endTimeMills
        return this
    }

    fun setOutputPath(output: String): FFmpegAudioTrimmer {
        this.outputPath = output
        return this
    }

    fun setOutputFileName(output: String): FFmpegAudioTrimmer {
        this.outputFileName = output
        return this
    }

    fun execute() {
        val outputLocation = getConvertedFile(outputPath, outputFileName)
        val audioPath = getPath(context, audioUri!!)

        val cmd = arrayOf(
            "-i",
            audioPath,
            "-ss",
            (startTime / 1000).toString(),
            "-t",
            ((endTime - startTime) / 1000).toString(),
            "-c",
            "copy",
            outputLocation.path
        )

        try {
            FFmpeg.getInstance(context).execute(cmd, object : ExecuteBinaryResponseHandler() {
                override fun onStart() {}

                override fun onProgress(message: String?) {
                    callback?.onProgress(message!!)
                }

                override fun onSuccess(message: String?) {
                    callback?.onSuccess(outputLocation, ContentType.AUDIO)
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
