package com.shykun.volodymyr.ffmpeglib.ffmpeg.audio

import android.content.Context
import android.net.Uri
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException
import com.shykun.volodymyr.ffmpeglib.ContentType
import com.shykun.volodymyr.ffmpeglib.ffmpeg.FFMpegCallback
import com.shykun.volodymyr.ffmpeglib.ffmpeg.FFmpegBase
import com.shykun.volodymyr.ffmpeglib.getPath
import java.io.IOException

class FFmpegAudioTrimmer(context: Context, videoUri: Uri, callback: FFMpegCallback) : FFmpegBase(context, videoUri, callback) {
    private var audioUri: Uri? = null
    private var startTime = 0
    private var endTime = 0

    fun setAudioUri(audioUri: Uri): FFmpegAudioTrimmer {
        this.audioUri = audioUri
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

    override fun getCommand(): Array<String?> {
        val outputLocation = getOutputLocation()
        val audioPath = getPath(context, audioUri!!)

        return arrayOf(
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
    }

    override fun getContentType(): ContentType = ContentType.AUDIO
}
