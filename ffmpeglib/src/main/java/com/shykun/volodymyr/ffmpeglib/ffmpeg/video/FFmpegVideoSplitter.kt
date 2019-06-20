package com.shykun.volodymyr.ffmpeglib.ffmpeg.video

import android.content.Context
import android.net.Uri
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException
import com.shykun.volodymyr.ffmpeglib.ContentType
import com.shykun.volodymyr.ffmpeglib.ffmpeg.FFMpegCallback
import com.shykun.volodymyr.ffmpeglib.ffmpeg.FFmpegBase
import com.shykun.volodymyr.ffmpeglib.getConvertedFile
import com.shykun.volodymyr.ffmpeglib.getPath
import java.io.File
import java.io.IOException

class FFmpegVideoSplitter(context: Context, videoUri: Uri, callback: FFMpegCallback) : FFmpegBase(context, videoUri, callback) {

    private var segmentTime = 0

    fun setSegmentTime(segmentTimeInSec: Int): FFmpegVideoSplitter {
        this.segmentTime = segmentTimeInSec
        return this
    }

    override fun getCommand(): Array<String?> {
        val outputLocation = getOutputLocation()
        val path = getPath(context, videoUri)

        return arrayOf(
            "-i",
            path,
            "-c",
            "copy",
            "-map",
            "0",
            "-segment_time",
            segmentTime.toString(),
            "-f",
            "segment",
            outputLocation.path + File.separator + outputFileName + "%03d.mp4"
        )
    }

    override fun getContentType(): ContentType = ContentType.MULTIPLE_VIDEO

    override fun getOutputLocation(): File {
        return getConvertedFile(outputPath, "")
    }
}