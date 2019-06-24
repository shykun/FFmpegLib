package com.shykun.volodymyr.videoeditor.di

import com.shykun.volodymyr.videoeditor.MainActivity
import com.shykun.volodymyr.videoeditor.fragment.ActionFragment
import com.shykun.volodymyr.videoeditor.fragment.FFmpegLoadingFragmemt
import com.shykun.volodymyr.videoeditor.fragment.RangeSeekBarFragment
import com.shykun.volodymyr.videoeditor.fragment.UploadFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NavigationModule::class])
interface AppComponent {
    fun inject(target: MainActivity)
    fun inject(target: UploadFragment)
    fun inject(target: ActionFragment)
    fun inject(target: RangeSeekBarFragment)
    fun inject(target: FFmpegLoadingFragmemt)
}