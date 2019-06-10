package com.shykun.volodymyr.videoeditor

data class Action(val name: String, val iconResourceId: Int)

val actions = arrayOf(
    Action("Cut", R.drawable.ic_cut_white_24dp),
    Action("Slow Motion", R.drawable.ic_slow_motion_24dp),
    Action("Fast Motion", R.drawable.ic_error_outline_black_24dp),
    Action("Extract Images", R.drawable.ic_image_24dp),
    Action("Extract Audio", R.drawable.ic_audio_24dp),
    Action("Reverse Video", R.drawable.ic_reverse_24dp),
    Action("Split Video", R.drawable.ic_split_video_24dp),
    Action("Resize", R.drawable.ic_resize_24dp),
    Action("Convert to GIF", R.drawable.ic_gif_24dp)
)