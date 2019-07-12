package com.shykun.volodymyr.ffmpeglib

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream

fun getOutputPath(folderName: String = "VideoEditor"): String {
    val path =
        Environment.getExternalStorageDirectory().toString() + File.separator + folderName + File.separator

    val folder = File(path)
    if (!folder.exists())
        folder.mkdirs()

    return path
}

fun copyFileToExternalStorage(resourceId: Int, resourceName: String, context: Context): File {
    val pathSDCard = getOutputPath() + resourceName
    try {
        val inputStream = context.resources.openRawResource(resourceId)
        inputStream.toFile(pathSDCard)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return File(pathSDCard)
}

fun InputStream.toFile(path: String) {
    File(path).outputStream().use { this.copyTo(it) }
}

fun getConvertedFile(folder: String, fileName: String): File {
    val f = File(folder)

    if (!f.exists())
        f.mkdirs()

    return File(f.path + File.separator + fileName)
}



