package com.shykun.volodymyr.videoeditor

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.shykun.volodymyr.videoeditor.di.AppComponent
import com.shykun.volodymyr.videoeditor.di.DaggerAppComponent
import com.shykun.volodymyr.videoeditor.di.NavigationModule
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var cicerone: Cicerone<Router>
    @Inject
    lateinit var navigator: FlowNavigator
    @Inject
    lateinit var router: Router

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
        showUloadFragment()
    }

    override fun onResume() {
        super.onResume()
        cicerone.navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        cicerone.navigatorHolder.removeNavigator()
        super.onPause()
    }

    fun showUloadFragment() {

        router.navigateTo(UPLOAD_FRAGMENT_KEY)
    }

    fun showOptionsFragment() {
        router.navigateTo(ACTION_FRAGMENT_KEY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}
