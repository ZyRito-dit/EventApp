package com.example.eventapp.ViewModel

import androidx.lifecycle.ViewModel

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.eventapp.datastore.SettingPreferences
import kotlinx.coroutines.launch

class SettingViewModel(private val pref: SettingPreferences) : ViewModel() {

    val themeSetting = pref.themeSetting.asLiveData()
    val reminderSetting = pref.reminderSetting.asLiveData()

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    fun saveReminderSetting(isReminderActive: Boolean) {
        viewModelScope.launch {
            pref.saveReminderSetting(isReminderActive)
        }
    }
}
