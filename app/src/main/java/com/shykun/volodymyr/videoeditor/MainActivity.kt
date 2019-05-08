package com.shykun.volodymyr.videoeditor

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showUloadFragment()
    }

    fun showUloadFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainFragmentContainer, UploadFragment())
            .commit()
    }

    fun showOptionsFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainFragmentContainer, ActionFragment())
            .commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}
