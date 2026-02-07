package com.miller198.audiovisualizsample.domain

import kotlinx.coroutines.flow.Flow

interface SettingRepository {
    suspend fun saveEffectSetting(preference: PlayerPreference): Result<Unit>
    suspend fun getEffectSetting(): Result<Flow<PlayerPreference>>
}
