package com.miller198.audiovisualizsample.presentation.screen.setting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.miller198.audiovisualizsample.MyApplication
import com.miller198.audiovisualizsample.domain.PlayerPreference
import com.miller198.audiovisualizsample.domain.SettingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

object SettingViewModelFactory {
    val Factory = viewModelFactory {
        initializer {
            val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)

            SettingViewModel(app.settingRepository)
        }
    }
}

class SettingViewModel @Inject constructor(
    private val repository: SettingRepository,
) : ViewModel() {

    private val _playerPreference = MutableStateFlow<PlayerPreference>(PlayerPreference.BAR)
    val playerPreference: StateFlow<PlayerPreference> = _playerPreference

    init {
        loadPlayerPreference()
    }

    private fun loadPlayerPreference() {
        viewModelScope.launch {
            val pref = repository.getEffectSetting()
                .onSuccess { pref ->
                    pref.collect {
                        _playerPreference.emit(it)
                    }
                }
                .onFailure {
                    Log.w("SettingViewModel", "Load Player Setting Failed")
                }
        }
    }

    fun savePlayerPreference(playerPreference: PlayerPreference) {
        viewModelScope.launch {
            repository.saveEffectSetting(playerPreference)
                .onSuccess {
                    _playerPreference.emit(playerPreference)
                }
                .onFailure {
                    Log.w("SettingViewModel", "Save Player Setting Failed")
                }
        }
    }
}
