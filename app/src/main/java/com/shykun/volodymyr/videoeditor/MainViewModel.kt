package com.shykun.volodymyr.videoeditor

import android.arch.lifecycle.ViewModel
import android.net.Uri

class MainViewModel : ViewModel() {

    lateinit var selectedVideoUri: Uri
}