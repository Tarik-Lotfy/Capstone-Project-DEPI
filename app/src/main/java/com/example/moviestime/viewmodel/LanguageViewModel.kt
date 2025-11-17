package com.example.moviestime.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.example.moviestime.data.datastore.LanguageSettingsDataStore

class LanguageViewModel(application: Application) : AndroidViewModel(application) {
    private val dataStore = LanguageSettingsDataStore(application)

    val appLanguage: StateFlow<String> = dataStore.appLanguage
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "en"
        )

    fun setAppLanguage(language: String) {
        viewModelScope.launch {
            dataStore.setAppLanguage(language)
        }
    }
}