package com.shykun.volodymyr.ffmpeglib.ffmpeg.audio

import android.content.Context
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException
import com.shykun.volodymyr.ffmpeglib.ContentType
import com.shykun.volodymyr.ffmpeglib.ffmpeg.FFmpegBase
import com.shykun.volodymyr.ffmpeglib.getConvertedFile
import com.shykun.volodymyr.ffmpeglib.getPath
import java.io.IOException

class FFmpegAudioExtractor (context: Context) : FFmpegBase(context) {
    override fun getContentType(): ContentType = ContentType.AUDIO

    override fun getCommand(): Array<String?> {
        val inputLocation = getPath(context, videoUri!!)
        val outputLocation = getOutputLocation()

        //Create Audio File with 192Kbps
        //Select .mp3 format
        return arrayOf(
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
    }

}