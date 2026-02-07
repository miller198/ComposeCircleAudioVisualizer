package com.miller198.audiovisualizsample

import android.app.Application
import com.miller198.audiovisualizsample.data.SettingRepositoryImpl
import com.miller198.audiovisualizsample.domain.SettingRepository

class MyApplication : Application() {
    val settingRepository: SettingRepository by lazy {
        SettingRepositoryImpl(this)
    }
}
