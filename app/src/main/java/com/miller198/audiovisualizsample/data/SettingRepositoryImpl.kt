package com.miller198.audiovisualizsample.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.miller198.audiovisualizsample.domain.PlayerPreference
import com.miller198.audiovisualizsample.domain.SettingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingRepositoryImpl(
    context: Context
): SettingRepository {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = SETTING_PREF_NAME)
    private val dataStore = context.dataStore

    override suspend fun saveEffectSetting(preference: PlayerPreference): Result<Unit> {
        return runCatching {
            dataStore.edit { pref ->
                pref[PLAYER_EFFECT] = preference.name
            }
        }
    }

    override suspend fun getEffectSetting(): Result<Flow<PlayerPreference>> {
        return runCatching {
            dataStore.data.map { pref ->
                PlayerPreference.valueOf(pref[PLAYER_EFFECT] ?: "BAR")
            }
        }
    }

    companion object {
        private const val SETTING_PREF_NAME = "settings"
        val PLAYER_EFFECT = stringPreferencesKey("player_effect")
    }
}
