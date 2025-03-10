package com.example.eventapp.ViewModelFactory


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eventapp.ViewModel.SettingViewModel
import com.example.eventapp.datastore.SettingPreferences

class SettingViewModelFactory(private val pref: SettingPreferences) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
