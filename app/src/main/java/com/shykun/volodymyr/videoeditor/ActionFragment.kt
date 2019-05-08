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


class ActionFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var updateHandler: Handler
    private var tmp: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(activity!!)
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
        videoView.setVideoURI(viewModel.selectedVideoUri)
        setPlayPauseButtonListener()
        videoView.setOnPreparedListener { mp ->
            seekBar.max = videoView.duration
            val minutes = videoView.duration / 60000
            val seconds = (videoView.duration % 60000) / 1000
            duration.text = "$minutes : $seconds"
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
                    currentTime.text = "$minute : $second"
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
        playStopButton.setOnClickListener {
            if (videoView.isPlaying)
                pauseVideo()
            else
                playVideo()
        }
    }

    private fun playVideo() {
        videoView.start()
        playStopButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pause_white_24dp, 0, 0, 0)
    }

    private fun pauseVideo() {
        videoView.pause()
        playStopButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_play_arrow_white_24dp, 0, 0, 0)
    }

}
