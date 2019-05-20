package com.shykun.volodymyr.videoeditor

import ru.terrakok.cicerone.android.support.SupportAppScreen

object UploadScreen : SupportAppScreen() {
    override fun getFragment() = UploadFragment()
}

object ActionScreen : SupportAppScreen() {
    override fun getFragment() = ActionFragment()
}

object CutScreen : SupportAppScreen() {
    override fun getFragment() = CutFragment()
}