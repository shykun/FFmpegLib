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
import com.shykun.volodymyr.videoeditor.dialog.SpecifyActionDialog
import com.shykun.volodymyr.videoeditor.dialog.SpecifyDialogClickListener
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
    private lateinit var ffmpegExecutor: FFmpegExecutor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity as MainActivity).component?.inject(this)

        mainViewModel = ViewModelProviders.of(activity!!)
            .get(MainViewModel::class.java)
//        fFmpegExecutor = (activity as MainActivity).ffmpeg
        actionAdapter = ActionAdapter(getActions(context!!))

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
            endTime.text =  getFormattedTime(videoView.duration)

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
                startTime.text = getFormattedTime(videoView.currentPosition)
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
                "Extract Images" -> performExtractImagesAction()
                "Extract Audio" -> performExtractAudioAction()
                "Reverse Video" -> performReverseUseCase()
                "Split Video" -> perfomSplitVideoUseCase()
                "Resize" -> perfomResizeVideoUseCase()
                "Convert to GIF" -> performConvertToGifUseCase()
            }
        }
    }

    private fun performCutAction() = navController.navigate(R.id.cutFragment)

    private fun performSlowMotionAction() =
        SlowMotionUseCase(mainViewModel.selectedVideoUri.value!!, context!!).execute()

    private fun performFastMotionAction() =
        FastMotionUseCase(mainViewModel.selectedVideoUri.value!!, context!!).execute()

    //    private fun performExtractImagesAction() = ExtractImagesUseCase(mainViewModel.selectedVideoUri.value!!, context!!).execute(0.1)
    private fun performExtractImagesAction() {
        val dialog = SpecifyActionDialog.newInstance("Set extract interval",  "Interval")
        dialog.specifyDialogClickListener = object : SpecifyDialogClickListener {
            override fun onConfirmClicked(input: String) {
                ExtractImagesUseCase(mainViewModel.selectedVideoUri.value!!, context!!).execute(input.toDouble())
            }
        }
        dialog.show(childFragmentManager, "tag")
    }

    private fun performExtractAudioAction() =
        ExtractAudioUseCase(mainViewModel.selectedVideoUri.value!!, context!!).execute()

    private fun performReverseUseCase() = ReverseUseCase(mainViewModel.selectedVideoUri.value!!, context!!).execute()

//    private fun perfomSplitVideoUseCase() =
//        SplitVideoUseCase(mainViewModel.selectedVideoUri.value!!, context!!).execute(10)

    private fun perfomSplitVideoUseCase() {
        val dialog = SpecifyActionDialog.newInstance("Set split interval", "Interval")
        dialog.specifyDialogClickListener = object : SpecifyDialogClickListener {
            override fun onConfirmClicked(input: String) {
                SplitVideoUseCase(mainViewModel.selectedVideoUri.value!!, context!!).execute(input.toInt())
            }
        }
        dialog.show(childFragmentManager, "tag")
    }

//    private fun perfomResizeVideoUseCase() =
//        ResizeVideoUseCase(mainViewModel.selectedVideoUri.value!!, context!!).execute("320:480")

    private fun perfomResizeVideoUseCase() {
        val dialog = SpecifyActionDialog.newInstance("Set output resolution", "Resolution")
        dialog.specifyDialogClickListener = object : SpecifyDialogClickListener {
            override fun onConfirmClicked(input: String) {
                ResizeVideoUseCase(mainViewModel.selectedVideoUri.value!!, context!!).execute("320:480")
            }
        }
        dialog.show(childFragmentManager, "tag")
    }

    private fun performConvertToGifUseCase() =
        ConvertToGifUseCase(mainViewModel.selectedVideoUri.value!!, context!!).execute()
}
