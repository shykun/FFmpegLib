package com.shykun.volodymyr.videoeditor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shykun.volodymyr.videoeditor.di.AppComponent
import com.shykun.volodymyr.videoeditor.di.DaggerAppComponent
import com.shykun.volodymyr.videoeditor.di.NavigationModule

class MainActivity : AppCompatActivity() {
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
    }
}
