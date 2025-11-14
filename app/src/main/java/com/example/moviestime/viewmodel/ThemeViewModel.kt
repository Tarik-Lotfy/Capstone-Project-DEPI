package com.example.moviestime.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.example.moviestime.data.datastore.ThemeSettingsDataStore

class ThemeViewModel(application: Application) : AndroidViewModel(application) {
    private val dataStore = ThemeSettingsDataStore(application)

    val isDarkThemeEnabled: StateFlow<Boolean> = dataStore.isDarkThemeEnabled
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    fun setDarkThemeEnabled(enabled: Boolean) {
        viewModelScope.launch {
            dataStore.setDarkThemeEnabled(enabled)
        }
    }
}