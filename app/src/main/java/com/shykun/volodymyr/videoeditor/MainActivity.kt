package com.shykun.volodymyr.videoeditor

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler
import com.shykun.volodymyr.ffmpeglib.FFmpegExecutor
import com.shykun.volodymyr.videoeditor.di.AppComponent
import com.shykun.volodymyr.videoeditor.di.DaggerAppComponent
import com.shykun.volodymyr.videoeditor.di.NavigationModule
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.Router
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var cicerone: Cicerone<Router>
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var router: Router

    lateinit var ffmpeg: FFmpegExecutor

    private lateinit var viewModel: MainViewModel

    @Inject
    lateinit var navController: NavController

    val component: AppComponent? by lazy {
        this.let {
            DaggerAppComponent.builder()
                .navigationModule(NavigationModule(this, supportFragmentManager, R.id.mainFragmentContainer))
                .build()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        component?.inject(this)
        viewModel = ViewModelProviders.of(this)
            .get(MainViewModel::class.java)


        subscribeVideoUriLiveDatat()
    }

    override fun onResume() {
        super.onResume()
        cicerone.navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        cicerone.navigatorHolder.removeNavigator()
        super.onPause()
    }

    private fun subscribeVideoUriLiveDatat() {
        viewModel.selectedVideoUri.observe(this, Observer { uri ->
            ffmpeg = FFmpegExecutor(this, uri)
                .loadBinary(object : LoadBinaryResponseHandler() {
                    override fun onFinish() {
                        super.onFinish()
                    }

                    override fun onSuccess() {
                        super.onSuccess()
                    }

                    override fun onFailure() {
                        super.onFailure()
                    }

                    override fun onStart() {
                        super.onStart()
                    }
                })
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

//    override fun onBackPressed() {
//        val currentFragment = supportFragmentManager.findFragmentById(R.id.mainFragmentContainer)
//        if (currentFragment is BackButtonListener && !currentFragment.onBackClicked())
//            finish()
//    }
}
