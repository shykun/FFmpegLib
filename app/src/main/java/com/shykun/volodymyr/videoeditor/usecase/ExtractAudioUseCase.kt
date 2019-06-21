package com.shykun.volodymyr.videoeditor.usecase

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.shykun.volodymyr.ffmpeglib.ContentType
import com.shykun.volodymyr.ffmpeglib.ffmpeg.FFMpegCallback
import com.shykun.volodymyr.ffmpeglib.ffmpeg.audio.FFmpegAudioExtractor
import com.shykun.volodymyr.ffmpeglib.getOutputPath
import com.shykun.volodymyr.videoeditor.getProgressDialog
import java.io.File


class ExtractAudioUseCase(
    private val context: Context,
    private val videoUri: Uri,
    private val callback: FFMpegCallback
) : BaseUseCase {

    override fun execute() {
        FFmpegAudioExtractor(context, videoUri, callback)
            .setOutputPath(getOutputPath() + "audio")
            .setOutputFileName("audio_" + System.currentTimeMillis() + ".mp3")
            .execute()
    }
}