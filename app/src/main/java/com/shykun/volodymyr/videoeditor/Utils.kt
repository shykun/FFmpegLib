package com.shykun.volodymyr.videoeditor

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File

fun getProgressDialog(context: Context): AlertDialog = AlertDialog.Builder(context)
    .setView(R.layout.dialog_progress)
    .setTitle(context.getString(R.string.title_progress_dialog))
    .setCancelable(false)
    .create()

fun getActions(context: Context): Array<Action> {
    return arrayOf(
        Action(context.getString(R.string.action_cut), R.drawable.ic_cut_white_24dp),
        Action(context.getString(R.string.action_slow_motion), R.drawable.ic_slow_motion_24dp),
        Action(context.getString(R.string.action_slow_resize), R.drawable.ic_slow_motion_24dp),
        Action(context.getString(R.string.action_fast_motion), R.drawable.ic_error_outline_black_24dp),
        Action(context.getString(R.string.action_fast_resize), R.drawable.ic_error_outline_black_24dp),
        Action(context.getString(R.string.action_extract_images), R.drawable.ic_image_24dp),
        Action(context.getString(R.string.action_extract_audio), R.drawable.ic_audio_24dp),
        Action(context.getString(R.string.action_reverse_video), R.drawable.ic_reverse_24dp),
        Action(context.getString(R.string.action_split_video), R.drawable.ic_split_video_24dp),
        Action(context.getString(R.string.action_resize), R.drawable.ic_resize_24dp),
        Action(context.getString(R.string.action_convert_to_gif), R.drawable.ic_gif_24dp),
        Action(context.getString(R.string.action_add_text), R.drawable.ic_text_24dp)
    )
}


fun openResult(context: Context?, convertedFile: File, type: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    val apkURI = context?.let {
        FileProvider.getUriForFile(
            it,
            it.applicationContext.packageName + ".provider",
            convertedFile
        )
    }
    intent.setDataAndType(apkURI, type)
    context?.startActivity(intent)
}


fun getFormattedTime(time: Int): String {
    val minutes = time / 60000
    val seconds = (time % 60000) / 1000
    return String.format("%d:%02d", minutes, seconds)
}