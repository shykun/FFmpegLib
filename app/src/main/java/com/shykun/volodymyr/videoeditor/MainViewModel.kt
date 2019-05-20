package com.shykun.volodymyr.videoeditor

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    var selectedVideoUri = MutableLiveData<Uri>()
}