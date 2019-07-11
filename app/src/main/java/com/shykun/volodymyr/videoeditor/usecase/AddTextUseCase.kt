package com.shykun.volodymyr.videoeditor.usecase

import android.content.Context
import android.net.Uri
import com.shykun.volodymyr.ffmpeglib.copyFileToExternalStorage
import com.shykun.volodymyr.ffmpeglib.ffmpeg.FFMpegCallback
import com.shykun.volodymyr.ffmpeglib.ffmpeg.video.AddedText
import com.shykun.volodymyr.ffmpeglib.ffmpeg.video.FFmpegTextOnVideo
import com.shykun.volodymyr.ffmpeglib.getOutputPath
import com.shykun.volodymyr.videoeditor.R

class AddTextUseCase(
    private val context: Context,
    private val videoUri: Uri,
    private val texts: List<AddedText?>,
    private val callback: FFMpegCallback
) : BaseUseCase {
    override fun execute() {
        val font = copyFileToExternalStorage(R.font.roboto_black, "myFont.ttf", context)

        FFmpegTextOnVideo(context, videoUri, callback)
            .setTexts(texts)
            .setFont(font)
            .setOutputPath(getOutputPath() + "video")
            .setOutputFileName("video_with_text_" + System.currentTimeMillis() + ".mp4")
            .execute()
    }
}