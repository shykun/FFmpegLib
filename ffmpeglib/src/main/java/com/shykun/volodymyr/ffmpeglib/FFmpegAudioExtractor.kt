package com.shykun.volodymyr.ffmpeglib

import android.content.Context
import android.net.Uri
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException
import java.io.IOException

class FFmpegAudioExtractor (private val context: Context) {

    private var videoUri: Uri? = null
    private var callback: FFMpegCallback? = null
    private var outputPath = ""
    private var outputFileName = ""

    fun setVideoUri(videoUri: Uri): FFmpegAudioExtractor {
        this.videoUri = videoUri
        return this
    }

    fun setCallback(callback: FFMpegCallback): FFmpegAudioExtractor {
        this.callback = callback
        return this
    }

    fun setOutputPath(output: String): FFmpegAudioExtractor {
        this.outputPath = output
        return this
    }

    fun setOutputFileName(output: String): FFmpegAudioExtractor {
        this.outputFileName = output
        return this
    }

    fun execute() {
            val inputLocation = getPath(context, videoUri!!)
            val outputLocation = getConvertedFile(outputPath, outputFileName)

            //Create Audio File with 192Kbps
            //Select .mp3 format
            val cmd = arrayOf(
                "-i",
                inputLocation,
                "-vn",
                "-ar",
                "44100",
                "-ac",
                "2",
                "-ab",
                "192",
                "-f",
                "mp3",
                outputLocation.path
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
                        callback?.onSuccess(outputLocation, ContentType.AUDIO)

                    }

                    override fun onFailure(message: String?) {
                        if (outputLocation.exists()) {
                            outputLocation.delete()
                        }
                        callback?.onFailure(IOException(message))
                    }

                    override fun onFinish() {
                        callback?.onFinish(outputLocation.path)
                    }
                })
            } catch (e: Exception) {
                callback!!.onFailure(e)
            } catch (e2: FFmpegCommandAlreadyRunningException) {
                callback!!.onNotAvailable(e2)
            }
    }
}