package com.example.moviestime.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.profileDataStore: DataStore<Preferences> by preferencesDataStore(name = "profile_data")

class ProfileDataStore(private val context: Context) {

    private fun nameKey(uid: String) = stringPreferencesKey("name_${uid}")
    private fun bioKey(uid: String) = stringPreferencesKey("bio_${uid}")
    private fun photoKey(uid: String) = stringPreferencesKey("photo_${uid}")

    fun profileFlow(uid: String, email: String, defaultName: String): Flow<UserProfileLocal> {
        return context.profileDataStore.data.map { prefs ->
            val name = prefs[nameKey(uid)] ?: defaultName
            val bio = prefs[bioKey(uid)] ?: ""
            val photoUrl = prefs[photoKey(uid)]
            UserProfileLocal(name = name, bio = bio, photoUrl = photoUrl, email = email)
        }
    }

    suspend fun saveLocal(uid: String, name: String?, bio: String?, photoUrl: String?) {
        context.profileDataStore.edit { prefs ->
            if (name != null) prefs[nameKey(uid)] = name
            if (bio != null) prefs[bioKey(uid)] = bio
            if (photoUrl != null) prefs[photoKey(uid)] = photoUrl
        }
    }
}

data class UserProfileLocal(
    val name: String,
    val bio: String,
    val photoUrl: String?,
    val email: String
)

