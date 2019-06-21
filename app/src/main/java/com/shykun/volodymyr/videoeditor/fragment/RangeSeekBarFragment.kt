package com.shykun.volodymyr.videoeditor.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import com.shykun.volodymyr.videoeditor.BackButtonListener
import com.shykun.volodymyr.videoeditor.MainActivity
import com.shykun.volodymyr.videoeditor.MainViewModel
import com.shykun.volodymyr.videoeditor.R
import kotlinx.android.synthetic.main.fragment_cut.*
import javax.inject.Inject


abstract class RangeSeekBarFragment : Fragment(), BackButtonListener {

    @Inject
    lateinit var navController: NavController
    protected lateinit var mainViewModel: MainViewModel
    private lateinit var updateHandler: Handler
    private lateinit var updateRunnable: Runnable

    protected var curStartValue = 0
    protected var curEndValue = 0

    protected abstract val confirmAction: (() -> Unit)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity as MainActivity).component?.inject(this)

        mainViewModel = ViewModelProviders.of(activity!!)
            .get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cut, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupVideo()
        setConfirmCutClickListener()
    }

    override fun onPause() {
        super.onPause()

        updateHandler.removeCallbacks(updateRunnable)
    }

    private fun setupVideo() {
        cutVideoView.setVideoURI(mainViewModel.selectedVideoUri.value)
        setVideoOnPrepareListener()
        setPlayPauseButtonListener()
        setupRangeSeekbarChangeListener()
    }

    private fun setPlayPauseButtonListener() {
        cutVideoView.setOnClickListener {
            if (cutVideoView.isPlaying)
                cutVideoView.pause()
            else
                cutVideoView.start()
        }
    }

    private fun setupRangeSeekbarChangeListener() {
        rangeSeekbar.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            if (minValue.toInt() != curStartValue) {
                curStartValue = minValue.toInt()
                cutVideoView.seekTo(curStartValue)
            }

            curEndValue = maxValue.toInt()
        }
    }

    private fun setupUpdateHandler() {
        updateHandler = Handler()
        updateRunnable = object : Runnable {
            override fun run() {
                val startMinute = curStartValue / 60000
                val startSecond = (curStartValue % 60000) / 1000

                val endMinute = curEndValue / 60000
                val endSecond = (curEndValue % 60000) / 1000

                cutStartTime.text = "$startMinute:$startSecond"
                cutEndTime.text = "$endMinute:$endSecond"

                if (cutVideoView.currentPosition >= curEndValue)
                    cutVideoView.seekTo(curStartValue)

                updateHandler.postDelayed(this, 16)

            }
        }
        updateHandler.postDelayed(updateRunnable, 16)
    }

    private fun setVideoOnPrepareListener() {
        cutVideoView.setOnPreparedListener {
            rangeSeekbar.setMaxValue(cutVideoView.duration.toFloat())

            curEndValue = cutVideoView.duration

            val minutes = cutVideoView.duration / 60000
            val seconds = (cutVideoView.duration % 60000) / 1000

            cutStartTime.text = "0:0"
            cutEndTime.text = "$minutes:$seconds"

            it.setOnSeekCompleteListener {
                if (!cutVideoView.isPlaying)
                    cutVideoView.start()
            }
        }

        setupUpdateHandler()
    }

    private fun setConfirmCutClickListener() {
        confirmButton.setOnClickListener {
            confirmAction.invoke()
        }
    }

    override fun onBackClicked(): Boolean {
        navController.popBackStack()
        return true
    }


}
