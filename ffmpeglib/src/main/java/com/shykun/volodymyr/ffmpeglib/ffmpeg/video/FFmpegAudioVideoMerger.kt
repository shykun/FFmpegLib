package com.shykun.volodymyr.ffmpeglib.ffmpeg.video

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

//Megre audio and video

class FFmpegAudioVideoMerger (context: Context, videoUri: Uri, callback: FFMpegCallback) : FFmpegBase(context, videoUri, callback) {

    private var audioUri: Uri? = null

    fun setAudioUri(audioUri: Uri): FFmpegAudioVideoMerger {
        this.audioUri = audioUri
        return this
    }

    override fun getCommand(): Array<String?> {
        val outputLocation = getOutputLocation()
        val audioPath = getPath(context, audioUri!!)
        val videoPath = getPath(context, videoUri)

        return arrayOf(
            "-i",
            videoPath,
            "-i",
            audioPath,
            "-c:v", "copy",
            "-c:a",
            "aac",
            "-strict",
            "experimental",
            "-map",
            "0:v:0",
            "-map",
            "1:a:0",
            "-shortest",
            outputLocation.path
        )
    }

    override fun getContentType(): ContentType = ContentType.VIDEO
}