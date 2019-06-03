package com.shykun.volodymyr.videoeditor

import ru.terrakok.cicerone.Screen

data class Action(val name: String, val iconResourceId: Int)

val actions = arrayOf(
    Action("Cut", R.drawable.ic_cut_white_24dp),
    Action("Slow Motion", R.drawable.ic_error_outline_black_24dp),
    Action("Fast Motion", R.drawable.ic_error_outline_black_24dp),
    Action("Extract Images", R.drawable.ic_error_outline_black_24dp),
    Action("Extract Audio", R.drawable.ic_error_outline_black_24dp),
    Action("Reverse Video", R.drawable.ic_error_outline_black_24dp),
    Action("Split Video", R.drawable.ic_error_outline_black_24dp)
)