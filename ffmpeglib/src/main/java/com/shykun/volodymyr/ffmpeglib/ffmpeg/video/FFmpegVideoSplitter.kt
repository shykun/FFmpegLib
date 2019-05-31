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
import java.io.File
import java.io.IOException

class FFmpegVideoSplitter(private val context: Context) {

    private var videoUri: Uri? = null
    private var callback: FFMpegCallback? = null
    private var outputPath = ""
    private var outputFileName = ""
    private var segementTime = 0

    fun setVideoUri(videoUri: Uri): FFmpegVideoSplitter {
        this.videoUri = videoUri
        return this
    }

    fun setCallback(callback: FFMpegCallback): FFmpegVideoSplitter {
        this.callback = callback
        return this
    }

    fun setOutputPath(output: String): FFmpegVideoSplitter {
        this.outputPath = output
        return this
    }

    fun setOutputFileName(output: String): FFmpegVideoSplitter {
        this.outputFileName = output
        return this
    }

    fun setSegmentTime(segmentTimeInSec: Int): FFmpegVideoSplitter {
        this.segementTime = segmentTimeInSec
        return this
    }

    fun execute() {
        val outputLocation = getConvertedFile(outputPath, "")
        val path = getPath(context, videoUri!!)

        val command = arrayOf(
            "-i",
            path,
            "-c",
            "copy",
            "-map",
            "0",
            "-segment_time",
            segementTime.toString(),
            "-f",
            "segment",
            outputLocation.path + File.separator + outputFileName + "%03d.mp4"
        )

        try {
            FFmpeg.getInstance(context).execute(command, object : ExecuteBinaryResponseHandler() {
                override fun onStart() {}

                override fun onProgress(message: String?) {
                    callback?.onProgress(message!!)
                }

                override fun onSuccess(message: String?) {
                    callback?.onSuccess(outputLocation, ContentType.MULTIPLE_VIDEO)

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
            callback!!.onFailure(e)
        } catch (e2: FFmpegCommandAlreadyRunningException) {
            callback!!.onNotAvailable(e2)
        }

    }
}