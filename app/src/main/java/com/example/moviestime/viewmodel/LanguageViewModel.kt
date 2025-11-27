package com.example.moviestime.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class LanguageViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("settings", Context.MODE_PRIVATE)

    private val _currentLanguage = MutableStateFlow(prefs.getString("language", "en") ?: "en")
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    private val _shouldRecreate = MutableStateFlow(false)
    val shouldRecreate: StateFlow<Boolean> = _shouldRecreate.asStateFlow()

    fun toggleLanguage() {
        val newLanguage = if (_currentLanguage.value == "en") "ar" else "en"
        _currentLanguage.value = newLanguage
        prefs.edit().putString("language", newLanguage).apply()
        _shouldRecreate.value = true
    }

    fun onRecreated() {
        _shouldRecreate.value = false
    }
}