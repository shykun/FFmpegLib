package com.shykun.volodymyr.videoeditor


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import kotlinx.android.synthetic.main.fragment_action.*

const val ACTION_FRAGMENT_KEY = "action_fragment_key"

class ActionFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var updateHandler: Handler
    private var tmp: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProviders.of(activity!!)
            .get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_action, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupVideo()
        setupActions()
    }

    private fun setupVideo() {
        videoView.setVideoURI(mainViewModel.selectedVideoUri)
        setPlayPauseButtonListener()
        videoView.setOnPreparedListener { mp ->
            seekBar.max = videoView.duration
            val minutes = videoView.duration / 60000
            val seconds = (videoView.duration % 60000) / 1000
            endTime.text = "$minutes : $seconds"
            mp.isLooping = true
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onStopTrackingTouch(seekBar: SeekBar) {}

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    tmp = progress
                    videoView.seekTo(progress)
                }
            }
        })

        updateHandler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                if (videoView.isPlaying)
                    seekBar.progress = videoView.currentPosition
                val minute = videoView.currentPosition / 60000
                val second = (videoView.currentPosition % 60000) / 1000
                startTime.text = "$minute : $second"
                updateHandler.postDelayed(this, 16)
            }
        }
        updateHandler.postDelayed(runnable, 16)
    }

    private fun setupActions() {
        val actionAdapter = ActionAdapter()
        actionsList.apply {
            layoutManager = LinearLayoutManager(this@ActionFragment.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = actionAdapter
        }
    }

    private fun setPlayPauseButtonListener() {
        videoView.setOnClickListener {
            if (videoView.isPlaying)
                videoView.pause()
            else
                videoView.start()
        }
    }

}
