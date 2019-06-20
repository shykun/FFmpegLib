package com.shykun.volodymyr.ffmpeglib.ffmpeg.audio

import android.content.Context
import android.net.Uri
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException
import com.shykun.volodymyr.ffmpeglib.ContentType
import com.shykun.volodymyr.ffmpeglib.ffmpeg.FFmpegBase
import com.shykun.volodymyr.ffmpeglib.getPath
import java.io.IOException

class FFmpegAudioMerger(context: Context) : FFmpegBase(context) {

    private var audio1Uri: Uri? = null
    private var audio2Uri: Uri? = null

    fun setAudio1Uri(audio1Uri: Uri): FFmpegAudioMerger {
        this.audio1Uri = audio1Uri
        return this
    }

    fun setAudio2Uri(audio2Uri: Uri): FFmpegAudioMerger {
        this.audio2Uri = audio2Uri
        return this
    }

    override fun getCommand(): Array<String?> {
        val outputLocation = getOutputLocation()
        val audio1Path = getPath(context, audio1Uri!!)
        val audio2Path = getPath(context, audio2Uri!!)

        return arrayOf(
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
    }

    override fun getContentType(): ContentType = ContentType.AUDIO

}
