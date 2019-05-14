package com.shykun.volodymyr.videoeditor.di

import com.shykun.volodymyr.videoeditor.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NavigationModule::class])
interface AppComponent {
    fun inject(target: MainActivity)
}