package com.shykun.volodymyr.ffmpeglib.ffmpeg.image

import android.content.Context
import android.net.Uri
import com.shykun.volodymyr.ffmpeglib.ContentType
import com.shykun.volodymyr.ffmpeglib.ffmpeg.FFMpegCallback
import com.shykun.volodymyr.ffmpeglib.ffmpeg.FFmpegBase
import com.shykun.volodymyr.ffmpeglib.getConvertedFile
import com.shykun.volodymyr.ffmpeglib.getPath
import java.io.File

//Get images from video

class FFmpegVideoToImages(context: Context, videoUri: Uri, callback: FFMpegCallback) : FFmpegBase(context, videoUri, callback) {
    override fun getCommand(): Array<String?> {
        val inputLocation = getPath(context, videoUri)
        val outputLocation = getOutputLocation()

        return arrayOf(
            "-i",
            inputLocation,
            "-r",
            interval.toString(),
            outputLocation.path + File.separator + outputFileName + "_%04d.jpg"
        )
    }

    override fun getContentType(): ContentType = ContentType.IMAGES

    private var interval = 1.0

    override fun getOutputLocation(): File {
        return getConvertedFile(outputPath, "")
    }

    fun setInterval(interval: Double): FFmpegVideoToImages {
        this.interval = interval
        return this
    }

}
