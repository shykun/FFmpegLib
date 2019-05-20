package com.shykun.volodymyr.videoeditor.di

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import javax.inject.Singleton

@Module
class NavigationModule(val activity: FragmentActivity, val fragmentManager: FragmentManager, val containerId: Int) {
    @Provides
    @Singleton
    fun provideCicerone(): Cicerone<Router> = Cicerone.create()


    @Provides
    @Singleton
    fun providesRouter(cicerone: Cicerone<Router>): Router = cicerone.router


    @Provides
    @Singleton
    fun providesNavigatorHolder(cicerone: Cicerone<Router>): NavigatorHolder = cicerone.navigatorHolder


    @Provides
    @Singleton
    fun provideFlowNavigator(): Navigator = SupportAppNavigator(activity, fragmentManager, containerId)
}