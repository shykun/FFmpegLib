package com.shykun.volodymyr.videoeditor.di

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.shykun.volodymyr.videoeditor.R
import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import javax.inject.Singleton

@Module
class NavigationModule(val activity: FragmentActivity, val fragmentManager: FragmentManager, val containerId: Int) {
    @Provides
    @Singleton
    fun provideFlowNavigator(): Navigator = SupportAppNavigator(activity, fragmentManager, containerId)

    @Provides
    @Singleton
    fun provideNavController(): NavController = Navigation.findNavController(activity, R.id.nav_host_fragment)
}