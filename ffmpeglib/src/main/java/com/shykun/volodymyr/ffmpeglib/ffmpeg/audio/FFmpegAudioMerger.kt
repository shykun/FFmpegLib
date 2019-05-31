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

class FFmpegAudioMerger private constructor(private val context: Context) {

    private var audio1Uri: Uri? = null
    private var audio2Uri: Uri? = null
    private var callback: FFMpegCallback? = null
    private var outputPath = ""
    private var outputFileName = ""

    fun setAudio1Uri(audio1Uri: Uri): FFmpegAudioMerger {
        this.audio1Uri = audio1Uri
        return this
    }

    fun setAudio2Uri(audio2Uri: Uri): FFmpegAudioMerger {
        this.audio2Uri = audio2Uri
        return this
    }

    fun setCallback(callback: FFMpegCallback): FFmpegAudioMerger {
        this.callback = callback
        return this
    }

    fun setOutputPath(output: String): FFmpegAudioMerger {
        this.outputPath = output
        return this
    }

    fun setOutputFileName(output: String): FFmpegAudioMerger {
        this.outputFileName = output
        return this
    }

    fun execute() {
        val outputLocation = getConvertedFile(outputPath, outputFileName)
        val audio1Path = getPath(context, audio1Uri!!)
        val audio2Path = getPath(context, audio2Uri!!)

        val cmd = arrayOf(
            "-y",
            "-i",
            audio1Path,
            "-i",
            audio2Path,
            "-filter_complex",
            "amix=inputs=2:duration=first:dropout_transition=0",
            "-codec:a",
            "libmp3lame",
            "-q:a",
            "0",
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
