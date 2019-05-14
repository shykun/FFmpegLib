package com.shykun.volodymyr.videoeditor

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import ru.terrakok.cicerone.android.SupportAppNavigator

class FlowNavigator(activity: FragmentActivity, fm: FragmentManager, containerId: Int) :
    SupportAppNavigator(activity, fm, containerId) {

    override fun createActivityIntent(context: Context?, screenKey: String?, data: Any?): Intent? {
        return null
    }

    override fun createFragment(screenKey: String?, data: Any?): Fragment? {
        return when (screenKey) {
            UPLOAD_FRAGMENT_KEY -> UploadFragment()
            ACTION_FRAGMENT_KEY -> ActionFragment()
            CUT_FRAGMENT_KEY -> CutFragment()

            else -> null
        }
    }
}