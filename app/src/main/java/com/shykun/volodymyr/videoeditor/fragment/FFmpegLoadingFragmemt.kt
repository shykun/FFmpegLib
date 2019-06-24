package com.shykun.volodymyr.videoeditor.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.FFmpegLoadBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException
import com.shykun.volodymyr.videoeditor.MainActivity
import com.shykun.volodymyr.videoeditor.R
import kotlinx.android.synthetic.main.fragment_ffmpeg_load.*
import javax.inject.Inject

const val FFMPEG_TAG = "FFmpeg"

class FFmpegLoadingFragmemt : Fragment() {

    @Inject
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).component?.inject(this)
        loadFFmpegBianry()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ffmpeg_load, container, false)
    }

    private fun loadFFmpegBianry() {
        try {
            showLoading()
            FFmpeg.getInstance(activity).loadBinary(object : FFmpegLoadBinaryResponseHandler {
                override fun onFailure() {
                    Log.e(FFMPEG_TAG, getString(R.string.ffmpeg_load_fail))
                    showFailure()
                }

                override fun onSuccess() {
                    Log.i(FFMPEG_TAG, getString(R.string.ffmpeg_load_success))
                    navController.navigate(R.id.uploadFragment)
                }

                override fun onStart() {
                    Log.i(FFMPEG_TAG, getString(R.string.ffmpeg_laod_start))
                }

                override fun onFinish() {
                    Log.i(FFMPEG_TAG, getString(R.string.ffmpeg_load_finish))
                }
            })
        } catch (e: FFmpegNotSupportedException) {
            showFFmpegNotSupportedMessage()
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showLoading() {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
            ffmpegLoadingProgress.visibility = VISIBLE
            ffmpegLoadingFailure.visibility = GONE
            ffmpegNotSupported.visibility = GONE
        }
    }

    private fun showFailure() {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
            ffmpegLoadingFailure.visibility = VISIBLE
            ffmpegLoadingProgress.visibility = GONE
            ffmpegNotSupported.visibility = GONE
        }
    }

    private fun showFFmpegNotSupportedMessage() {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
            ffmpegNotSupported.visibility = VISIBLE
            ffmpegLoadingProgress.visibility = GONE
            ffmpegLoadingFailure.visibility = GONE
        }
    }
}
