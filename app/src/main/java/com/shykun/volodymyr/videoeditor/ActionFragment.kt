package com.shykun.volodymyr.videoeditor


import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.shykun.volodymyr.ffmpeglib.FFmpegExecutor
import com.shykun.volodymyr.videoeditor.usecase.*

import kotlinx.android.synthetic.main.fragment_action.*
import javax.inject.Inject

const val ACTION_FRAGMENT_KEY = "action_fragment_key"

class ActionFragment : Fragment() {

    @Inject
    lateinit var navController: NavController
    private lateinit var mainViewModel: MainViewModel
    private lateinit var actionAdapter: ActionAdapter

    private lateinit var updateHandler: Handler
    private lateinit var updateRunnable: Runnable
    private lateinit var fFmpegExecutor: FFmpegExecutor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity as MainActivity).component?.inject(this)

        mainViewModel = ViewModelProviders.of(activity!!)
            .get(MainViewModel::class.java)
//        fFmpegExecutor = (activity as MainActivity).ffmpeg
        actionAdapter = ActionAdapter()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_action, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupVideo()
        setupActions()
        setupActionClickListener()
    }

    override fun onPause() {
        super.onPause()

        updateHandler.removeCallbacks(updateRunnable)
    }

    private fun setupVideo() {
        videoView.setVideoURI(mainViewModel.selectedVideoUri.value)
        setupPlayPauseListener()
        videoView.setOnPreparedListener {
            seekBar.max = videoView.duration
            val minutes = videoView.duration / 60000
            val seconds = (videoView.duration % 60000) / 1000
            endTime.text = "$minutes : $seconds"

            it.setOnSeekCompleteListener {
                if (!videoView.isPlaying)
                    videoView.start()
            }

        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onStopTrackingTouch(seekBar: SeekBar) {}

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    videoView.seekTo(progress)
                }
            }
        })

        updateHandler = Handler()
        updateRunnable = object : Runnable {
            override fun run() {
                if (videoView.isPlaying)
                    seekBar.progress = videoView.currentPosition
                val minute = videoView.currentPosition / 60000
                val second = (videoView.currentPosition % 60000) / 1000
                startTime.text = "$minute : $second"
                updateHandler.postDelayed(this, 16)
            }
        }
        updateHandler.postDelayed(updateRunnable, 16)
    }

    private fun setupActions() {
        actionsList.apply {
            layoutManager = LinearLayoutManager(this@ActionFragment.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = actionAdapter
        }
    }

    private fun setupPlayPauseListener() {
        videoView.setOnClickListener {
            if (videoView.isPlaying)
                videoView.pause()
            else
                videoView.start()
        }
    }

    @SuppressLint("CheckResult")
    private fun setupActionClickListener() {
        actionAdapter.clickObservable.subscribe {

            when (it.name) {
                "Cut" -> performCutAction()
                "Slow Motion" -> performSlowMotionAction()
                "Fast Motion" -> performFastMotionAction()
                "Extract Images" -> performExtrtactImagesAction()
                "Extract Audio" -> perfomExtractAudioAction()
                "Reverse Video" -> performReverseUseCase()
            }
        }
    }

    private fun performCutAction() = navController.navigate(R.id.cutFragment)

    private fun performSlowMotionAction() = SlowMotionUseCase(fFmpegExecutor, context!!).execute()

    private fun performFastMotionAction() = FastMotionUseCase(fFmpegExecutor, context!!).execute()

    private fun performExtrtactImagesAction() = ExtractImagesUseCase(fFmpegExecutor, context!!).execute(0, videoView.duration)

    private fun perfomExtractAudioAction() = ExtractAudioUseCase(mainViewModel.selectedVideoUri.value!!, context!!).execute()

    private fun performReverseUseCase() = ReverseUseCase(fFmpegExecutor, context!!).execute()
}
