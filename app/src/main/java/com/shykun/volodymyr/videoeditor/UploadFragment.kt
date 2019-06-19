package com.shykun.volodymyr.videoeditor


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.fragment_upload.*
import ru.terrakok.cicerone.Router
import javax.inject.Inject

private const val REQUEST_TAKE_GALLERY_VIDEO = 100
const val UPLOAD_FRAGMENT_KEY = "upload_fragment_key"

class UploadFragment : Fragment() {

    @Inject
    lateinit var navController: NavController

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(activity!!)
            .get(MainViewModel::class.java)

        (activity as MainActivity).component?.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_upload, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUploadClickListener()
    }

    private fun setUploadClickListener() {
        uploadButton.setOnClickListener {
            RxPermissions(this)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe { granted ->
                    if (granted) {
                        uploadVideo()
                    }
                }
        }
    }

    private fun uploadVideo() {
        try {
            val intent = Intent()
            intent.type = "video/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, getString(R.string.title_select_video)), REQUEST_TAKE_GALLERY_VIDEO)
        } catch (e: Exception) {

        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                viewModel.selectedVideoUri.value = data!!.data

                navController.navigate(R.id.actionFragment)
            }
        }
    }
}
