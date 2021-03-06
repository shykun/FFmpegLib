package com.shykun.volodymyr.ffmpeglib.ffmpeg

import com.shykun.volodymyr.ffmpeglib.ContentType
import java.io.File

interface FFMpegCallback {
    fun onStartProcessing()

    fun onProgress(progress: String)

    fun onSuccess(convertedFile: File, contentType: ContentType)

    fun onFailure(error: Exception)

    fun onNotAvailable(error: Exception)

    fun onFinishProcessing()

}