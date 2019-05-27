package com.shykun.volodymyr.videoeditor

import ru.terrakok.cicerone.Screen

data class Action(val name: String, val iconResourceId: Int, val screen: Screen)

val actions = arrayOf(
    Action("Cut", R.drawable.ic_cut_white_24dp, CutScreen),
    Action("Slow Motion", R.drawable.ic_error_outline_black_24dp, CutScreen),
    Action("Fast Motion", R.drawable.ic_error_outline_black_24dp, CutScreen),
    Action("Extract Images", R.drawable.ic_error_outline_black_24dp, CutScreen),
    Action("Compress", R.drawable.ic_error_outline_black_24dp, CutScreen)
)