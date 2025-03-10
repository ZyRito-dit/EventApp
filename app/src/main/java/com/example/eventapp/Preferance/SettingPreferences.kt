package com.example.eventapp.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("settings")

class SettingPreferences private constructor(private val context: Context) {

    private val THEME_KEY = booleanPreferencesKey("theme_setting")


    val themeSetting: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[THEME_KEY] ?: false
    }


    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDarkModeActive
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingPreferences? = null

        fun getInstance(context: Context): SettingPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreferences(context)
                INSTANCE = instance
                instance
            }
        }
    }
}
