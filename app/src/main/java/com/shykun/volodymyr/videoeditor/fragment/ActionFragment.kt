package com.shykun.volodymyr.videoeditor.fragment


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.shykun.volodymyr.ffmpeglib.ContentType
import com.shykun.volodymyr.ffmpeglib.ffmpeg.FFMpegCallback

import com.shykun.volodymyr.videoeditor.*
import com.shykun.volodymyr.videoeditor.adapter.ActionAdapter

import com.shykun.volodymyr.videoeditor.dialog.SpecifyActionDialog
import com.shykun.volodymyr.videoeditor.dialog.SpecifyDialogClickListener
import com.shykun.volodymyr.videoeditor.usecase.*

import kotlinx.android.synthetic.main.fragment_action.*
import java.io.File
import javax.inject.Inject

const val ACTION_FRAGMENT_KEY = "action_fragment_key"

class ActionFragment : Fragment(), FFMpegCallback {
    private lateinit var progressDialog: AlertDialog

    override fun onStartProcessing() {
        progressDialog.show()
    }

    override fun onProgress(progress: String) {
        progressDialog.setMessage("progress : $progress")
    }

    override fun onSuccess(convertedFile: File, contentType: ContentType) {
        Toast.makeText(context, "SUCCESS", Toast.LENGTH_SHORT).show()

        when (contentType) {
            ContentType.VIDEO,
            ContentType.MULTIPLE_VIDEO -> {
                openResult(convertedFile, "video/mp4")
            }
            ContentType.AUDIO -> {
                openResult(convertedFile, "audio/mp3")
            }
            else -> {
            }
        }
    }

    override fun onFailure(error: Exception) {
        Toast.makeText(context, "FAILURE", Toast.LENGTH_SHORT).show()
    }

    override fun onNotAvailable(error: Exception) {
        Toast.makeText(context, "NOT AVAILABLE", Toast.LENGTH_SHORT).show()
    }

    override fun onFinishProcessing() {
        progressDialog.hide()
    }

    @Inject
    lateinit var navController: NavController
    private lateinit var mainViewModel: MainViewModel
    private lateinit var actionAdapter: ActionAdapter

    private lateinit var updateHandler: Handler
    private lateinit var updateRunnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity as MainActivity).component?.inject(this)

        mainViewModel = ViewModelProviders.of(activity!!)
                .get(MainViewModel::class.java)
        actionAdapter = ActionAdapter(getActions(context!!))

        progressDialog = getProgressDialog(context!!)
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
            endTime.text = getFormattedTime(videoView.duration)

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
                getString(R.string.action_cut) -> performCutAction()
                getString(R.string.action_slow_motion) -> performSlowMotionAction()
                getString(R.string.action_fast_motion) -> performFastMotionAction()
                getString(R.string.action_extract_images) -> performExtractImagesAction()
                getString(R.string.action_extract_audio) -> performExtractAudioAction()
                getString(R.string.action_reverse_video) -> performReverseUseCase()
                getString(R.string.action_split_video) -> performSplitVideoUseCase()
                getString(R.string.action_resize) -> performResizeVideoUseCase()
                getString(R.string.action_convert_to_gif) -> performConvertToGifUseCase()
                getString(R.string.action_slow_resize) -> performSlowResizeAction()
                getString(R.string.action_fast_resize) -> performFastResizeAction()
            }
        }
    }

    private fun performSlowResizeAction() {
        SlowMotionResizeUseCase(context!!, mainViewModel.selectedVideoUri.value!!, this).execute()
    }

    private fun performFastResizeAction() {
        FastMotionResizeUseCase(context!!, mainViewModel.selectedVideoUri.value!!, this).execute()
    }

    private fun performCutAction() = navController.navigate(R.id.cutFragment)

    private fun performSlowMotionAction() =
            SlowMotionOriginalUseCase(context!!, mainViewModel.selectedVideoUri.value!!, this).execute()

    private fun performFastMotionAction() =
            FastMotionOriginalUseCase(context!!, mainViewModel.selectedVideoUri.value!!, this).execute()

    //    private fun performExtractImagesAction() = ExtractImagesUseCase(mainViewModel.selectedVideoUri.value!!, context!!).execute(0.1)
    private fun performExtractImagesAction() {
        val dialog = SpecifyActionDialog.newInstance(
                getString(R.string.set_extract_interval), getString(
                R.string.interval
        )
        )
        dialog.specifyDialogClickListener = object : SpecifyDialogClickListener {
            override fun onConfirmClicked(input: String) {
                ExtractImagesUseCase(
                        context!!,
                        mainViewModel.selectedVideoUri.value!!,
                        this@ActionFragment,
                        input.toDouble()
                ).execute()

            }
        }
        dialog.show(childFragmentManager, "tag")
    }

    private fun performExtractAudioAction() =
            ExtractAudioUseCase(context!!, mainViewModel.selectedVideoUri.value!!, this).execute()

    private fun performReverseUseCase() =
            ReverseUseCase(context!!, mainViewModel.selectedVideoUri.value!!, this).execute()

    private fun performSplitVideoUseCase() {
        val dialog = SpecifyActionDialog.newInstance(
                getString(R.string.set_split_interval), getString(
                R.string.interval
        )
        )
        dialog.specifyDialogClickListener = object : SpecifyDialogClickListener {
            override fun onConfirmClicked(input: String) {
                SplitVideoUseCase(
                        context!!,
                        mainViewModel.selectedVideoUri.value!!,
                        this@ActionFragment,
                        input.toInt()
                ).execute()
            }
        }
        dialog.show(childFragmentManager, "tag")
    }

    private fun performResizeVideoUseCase() {
        val dialog = SpecifyActionDialog.newInstance(
                getString(R.string.set_output_resolution), getString(
                R.string.resolution
        )
        )
        dialog.specifyDialogClickListener = object : SpecifyDialogClickListener {
            override fun onConfirmClicked(input: String) {
                //TODO remove hardcoded value
                ResizeVideoUseCase(
                        context!!,
                        mainViewModel.selectedVideoUri.value!!,
                        this@ActionFragment,
                        "320:480"
                ).execute()
            }
        }
        dialog.show(childFragmentManager, "tag")
    }

    private fun performConvertToGifUseCase() =
            ConvertToGifUseCase(context!!, mainViewModel.selectedVideoUri.value!!, this).execute()

    private fun openResult(convertedFile: File, type: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        val apkURI = context?.let {
            FileProvider.getUriForFile(
                    it,
                    it.applicationContext
                            .packageName + ".provider", convertedFile
            )
        }
        intent.setDataAndType(apkURI, type)
        context?.startActivity(intent)
    }
}
