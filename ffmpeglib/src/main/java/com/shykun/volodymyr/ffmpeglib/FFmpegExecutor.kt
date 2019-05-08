package com.shykun.volodymyr.ffmpeglib

import android.content.Context
import android.net.Uri
import android.os.Environment
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler
import java.io.File
import org.apache.commons.io.comparator.LastModifiedFileComparator
import java.util.*

class FFmpegExecutor(private val context: Context, private val videoUri: Uri) {

    private lateinit var ffmpeg: FFmpeg

    //Handle FFmpegNotSupportedException
    fun loadBinary(loadBinaryResponseHandler: LoadBinaryResponseHandler): FFmpegExecutor {
        if (ffmpeg == null) {
            ffmpeg = FFmpeg.getInstance(context)
        }
        ffmpeg.loadBinary(loadBinaryResponseHandler)

        return this
    }

    /**
     * Command for cutting video
     */
    private fun executeCutVideoCommand(
        startMs: Int,
        endMs: Int,
        executeBinaryResponseHandler: ExecuteBinaryResponseHandler
    ) {
        val moviesDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_MOVIES
        )

        val filePrefix = "cut_video"
        val fileExtn = ".mp4"
        val yourRealPath = getPath(context, videoUri)
        var dest = File(moviesDir, filePrefix + fileExtn)
        var fileNo = 0
        while (dest.exists()) {
            fileNo++
            dest = File(moviesDir, filePrefix + fileNo + fileExtn)
        }

        val filePath = dest.absolutePath
        //String[] complexCommand = {"-i", yourRealPath, "-ss", "" + startMs / 1000, "-t", "" + endMs / 1000, dest.getAbsolutePath()};
        val complexCommand = arrayOf(
            "-ss",
            "" + startMs / 1000,
            "-y",
            "-i",
            yourRealPath,
            "-t",
            "" + (endMs - startMs) / 1000,
            "-vcodec",
            "mpeg4",
            "-b:v",
            "2097152",
            "-b:a",
            "48000",
            "-ac",
            "2",
            "-ar",
            "22050",
            filePath
        )

        ffmpeg.execute(complexCommand, executeBinaryResponseHandler)
    }

    /**
     * Command for compressing video
     */
    private fun executeCompressCommand(executeBinaryResponseHandler: ExecuteBinaryResponseHandler) {
        val moviesDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_MOVIES
        )

        val filePrefix = "compress_video"
        val fileExtn = ".mp4"
        val yourRealPath = getPath(context, videoUri)


        var dest = File(moviesDir, filePrefix + fileExtn)
        var fileNo = 0
        while (dest.exists()) {
            fileNo++
            dest = File(moviesDir, filePrefix + fileNo + fileExtn)
        }

        val filePath = dest.absolutePath
        val complexCommand = arrayOf(
            "-y",
            "-i",
            yourRealPath,
            "-s",
            "160x120",
            "-r",
            "25",
            "-vcodec",
            "mpeg4",
            "-b:v",
            "150k",
            "-b:a",
            "48000",
            "-ac",
            "2",
            "-ar",
            "22050",
            filePath
        )
        ffmpeg.execute(complexCommand, executeBinaryResponseHandler)
    }

    /**
     * Command for extracting images from video
     */
    private fun extractImagesVideo(
        startMs: Int,
        endMs: Int,
        executeBinaryResponseHandler: ExecuteBinaryResponseHandler
    ) {
        val moviesDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
        )

        val filePrefix = "extract_picture"
        val fileExtn = ".jpg"
        val yourRealPath = getPath(context, videoUri)

        var dir = File(moviesDir, "VideoEditor")
        var fileNo = 0
        while (dir.exists()) {
            fileNo++
            dir = File(moviesDir, "VideoEditor$fileNo")

        }
        dir.mkdir()
        val filePath = dir.absolutePath
        val dest = File(dir, "$filePrefix%03d$fileExtn")

        val complexCommand = arrayOf(
            "-y",
            "-i",
            yourRealPath,
            "-an",
            "-r",
            "1",
            "-ss",
            "" + startMs / 1000,
            "-t",
            "" + (endMs - startMs) / 1000,
            dest.absolutePath
        )
        /*   Remove -r 1 if you want to extract all video frames as images from the specified time duration.*/
        ffmpeg.execute(complexCommand, executeBinaryResponseHandler)
    }

    /**
     * Command for adding fade in fade out effect at start and end of video
     */
    private fun executeFadeInFadeOutCommand(executeBinaryResponseHandler: ExecuteBinaryResponseHandler, duration: Int) {
        val moviesDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_MOVIES
        )

        val filePrefix = "fade_video"
        val fileExtn = ".mp4"
        val yourRealPath = getPath(context, videoUri)


        var dest = File(moviesDir, filePrefix + fileExtn)
        var fileNo = 0
        while (dest.exists()) {
            fileNo++
            dest = File(moviesDir, filePrefix + fileNo + fileExtn)
        }

        val filePath = dest.absolutePath
        val complexCommand = arrayOf(
            "-y",
            "-i",
            yourRealPath,
            "-acodec",
            "copy",
            "-vf",
            "fade=t=in:st=0:d=5,fade=t=out:st=" + (duration - 5).toString() + ":d=5",
            filePath
        )
        ffmpeg.execute(complexCommand, executeBinaryResponseHandler)
    }

    /**
     * Command for creating fast motion video
     */
    private fun executeFastMotionVideoCommand(executeBinaryResponseHandler: ExecuteBinaryResponseHandler) {
        val moviesDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_MOVIES
        )

        val filePrefix = "speed_video"
        val fileExtn = ".mp4"
        val yourRealPath = getPath(context, videoUri)


        var dest = File(moviesDir, filePrefix + fileExtn)
        var fileNo = 0
        while (dest.exists()) {
            fileNo++
            dest = File(moviesDir, filePrefix + fileNo + fileExtn)
        }

        val filePath = dest.absolutePath
        val complexCommand = arrayOf(
            "-y",
            "-i",
            yourRealPath,
            "-filter_complex",
            "[0:v]setpts=0.5*PTS[v];[0:a]atempo=2.0[a]",
            "-map",
            "[v]",
            "-map",
            "[a]",
            "-b:v",
            "2097k",
            "-r",
            "60",
            "-vcodec",
            "mpeg4",
            filePath
        )
        ffmpeg.execute(complexCommand, executeBinaryResponseHandler)
    }

    /**
     * Command for creating slow motion video
     */
    private fun executeSlowMotionVideoCommand(executeBinaryResponseHandler: ExecuteBinaryResponseHandler) {
        val moviesDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_MOVIES
        )

        val filePrefix = "slowmotion_video"
        val fileExtn = ".mp4"
        val yourRealPath = getPath(context, videoUri)


        var dest = File(moviesDir, filePrefix + fileExtn)
        var fileNo = 0
        while (dest.exists()) {
            fileNo++
            dest = File(moviesDir, filePrefix + fileNo + fileExtn)
        }


        val filePath = dest.absolutePath
        val complexCommand = arrayOf(
            "-y",
            "-i",
            yourRealPath,
            "-filter_complex",
            "[0:v]setpts=2.0*PTS[v];[0:a]atempo=0.5[a]",
            "-map",
            "[v]",
            "-map",
            "[a]",
            "-b:v",
            "2097k",
            "-r",
            "60",
            "-vcodec",
            "mpeg4",
            filePath
        )
        ffmpeg.execute(complexCommand, executeBinaryResponseHandler)
    }

    /**
     * Command for extracting audio from video
     */
    private fun extractAudioVideo(executeBinaryResponseHandler: ExecuteBinaryResponseHandler) {
        val moviesDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_MUSIC
        )

        val filePrefix = "extract_audio"
        val fileExtn = ".mp3"
        val yourRealPath = getPath(context, videoUri)
        var dest = File(moviesDir, filePrefix + fileExtn)

        var fileNo = 0
        while (dest.exists()) {
            fileNo++
            dest = File(moviesDir, filePrefix + fileNo + fileExtn)
        }
        val filePath = dest.absolutePath

        val complexCommand =
            arrayOf("-y", "-i", yourRealPath, "-vn", "-ar", "44100", "-ac", "2", "-b:a", "256k", "-f", "mp3", filePath)

        ffmpeg.execute(complexCommand, executeBinaryResponseHandler)
    }

    /**
     * Command for segmenting video
     */
    private fun splitVideoCommand(path: String, executeBinaryResponseHandler: ExecuteBinaryResponseHandler) {
        val moviesDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_MOVIES
        )
        val filePrefix = "split_video"
        val fileExtn = ".mp4"

        val dir = File(moviesDir, ".VideoSplit")
        if (dir.exists())
            deleteDir(dir)
        dir.mkdir()
        val dest = File(dir, "$filePrefix%03d$fileExtn")

        val complexCommand = arrayOf(
            "-i",
            path,
            "-c:v",
            "libx264",
            "-crf",
            "22",
            "-map",
            "0",
            "-segment_time",
            "6",
            "-g",
            "9",
            "-sc_threshold",
            "0",
            "-force_key_frames",
            "expr:gte(t,n_forced*6)",
            "-f",
            "segment",
            dest.absolutePath
        )
        ffmpeg.execute(complexCommand, executeBinaryResponseHandler)
    }

    /**
     * Command for reversing segmented videos
     */
    private fun reverseVideoCommand(executeBinaryResponseHandler: ExecuteBinaryResponseHandler) {
        val moviesDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_MOVIES
        )
        val srcDir = File(moviesDir, ".VideoSplit")
        val files = srcDir.listFiles()
        val filePrefix = "reverse_video"
        val fileExtn = ".mp4"
        val destDir = File(moviesDir, ".VideoPartsReverse")
        if (destDir.exists())
            deleteDir(destDir)
        destDir.mkdir()
        for (i in files.indices) {
            val dest = File(destDir, filePrefix + i + fileExtn)
            val command = arrayOf("-i", files[i].absolutePath, "-vf", "reverse", "-af", "areverse", dest.absolutePath)
            if (i == files.size - 1)
            //lastReverseCommand = command ????
                ffmpeg.execute(command, executeBinaryResponseHandler)
        }
    }

    /**
     * Command for concating reversed segmented videos
     */
    private fun concatVideoCommand(executeBinaryResponseHandler: ExecuteBinaryResponseHandler) {
        val moviesDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_MOVIES
        )
        val srcDir = File(moviesDir, ".VideoPartsReverse")
        val files = srcDir.listFiles()
        if (files != null && files.size > 1) {
            Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE)
        }
        val stringBuilder = StringBuilder()
        val filterComplex = StringBuilder()
        filterComplex.append("-filter_complex,")
        for (i in files!!.indices) {
            stringBuilder.append("-i" + "," + files[i].absolutePath + ",")
            filterComplex.append("[").append(i).append(":v").append(i).append("] [").append(i).append(":a").append(i)
                .append("] ")

        }
        filterComplex.append("concat=n=").append(files.size).append(":v=1:a=1 [v] [a]")
        val inputCommand = stringBuilder.toString().split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val filterCommand = filterComplex.toString().split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val filePrefix = "reverse_video"
        val fileExtn = ".mp4"
        var dest = File(moviesDir, filePrefix + fileExtn)
        var fileNo = 0
        while (dest.exists()) {
            fileNo++
            dest = File(moviesDir, filePrefix + fileNo + fileExtn)
        }
        val filePath = dest.absolutePath
        val destinationCommand = arrayOf("-map", "[v]", "-map", "[a]", dest.absolutePath)
        ffmpeg.execute(combine(inputCommand, filterCommand, destinationCommand), executeBinaryResponseHandler)
    }

    fun combine(arg1: Array<String>, arg2: Array<String>, arg3: Array<String>): Array<String?> {
        val result = arrayOfNulls<String>(arg1.size + arg2.size + arg3.size)
        System.arraycopy(arg1, 0, result, 0, arg1.size)
        System.arraycopy(arg2, 0, result, arg1.size, arg2.size)
        System.arraycopy(arg3, 0, result, arg1.size + arg2.size, arg3.size)
        return result
    }
}