package com.example.moviestime.data.repository

import android.content.Context
import android.net.Uri
import com.example.moviestime.data.datastore.ProfileDataStore
import com.example.moviestime.data.datastore.UserProfileLocal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.coroutines.tasks.await

data class UserProfileData(
    val name: String,
    val bio: String,
    val photoUrl: String?,
    val email: String
)

class UserProfileRepository(private val context: Context) {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val store = ProfileDataStore(context)

    fun observeProfile(): Flow<UserProfileData> {
        val user = auth.currentUser
        val uid = user?.uid ?: ""
        val email = user?.email ?: ""
        val defaultName = user?.displayName ?: ""
        return store.profileFlow(uid, email, defaultName).map { local ->
            UserProfileData(
                name = local.name,
                bio = local.bio,
                photoUrl = local.photoUrl,
                email = local.email
            )
        }
    }

    suspend fun syncFromRemote() {
        val user = auth.currentUser ?: return
        val uid = user.uid
        val doc = db.collection("users").document(uid).get().await()
        val name = doc.getString("name") ?: user.displayName ?: ""
        val bio = doc.getString("bio") ?: ""
        val photoUrl = doc.getString("photoUrl") ?: user.photoUrl?.toString()
        store.saveLocal(uid, name = name, bio = bio, photoUrl = photoUrl)
    }

    suspend fun saveLocalOnly(name: String, bio: String, localPhotoUri: String?) {
        val user = auth.currentUser ?: return
        val uid = user.uid
        store.saveLocal(uid, name = name, bio = bio, photoUrl = localPhotoUri)
    }

    suspend fun saveRemote(name: String, bio: String, localPhotoUri: String?) {
        val user = auth.currentUser ?: return
        val uid = user.uid
        withContext(Dispatchers.IO) {
            val updates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
            user.updateProfile(updates).await()

            var finalPhotoUrl: String? = null
            if (localPhotoUri != null) {
                val ref = storage.reference.child("users/$uid/profile_${System.currentTimeMillis()}.jpg")
                ref.putFile(Uri.parse(localPhotoUri)).await()
                finalPhotoUrl = ref.downloadUrl.await().toString()
            }

            val data = hashMapOf(
                "name" to name,
                "bio" to bio,
                "email" to user.email
            )
            if (finalPhotoUrl != null) data["photoUrl"] = finalPhotoUrl

            db.collection("users").document(uid).set(data, SetOptions.merge()).await()

            store.saveLocal(uid, name = name, bio = bio, photoUrl = finalPhotoUrl ?: localPhotoUri)
        }
    }

    suspend fun saveProfile(name: String, bio: String, localPhotoUri: String?) {
        val user = auth.currentUser ?: return
        val uid = user.uid
        store.saveLocal(uid, name = name, bio = bio, photoUrl = localPhotoUri)

        withContext(Dispatchers.IO) {
            val updates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
            user.updateProfile(updates).await()

            var finalPhotoUrl: String? = null
            if (localPhotoUri != null) {
                val ref = storage.reference.child("users/$uid/profile_${System.currentTimeMillis()}.jpg")
                ref.putFile(Uri.parse(localPhotoUri)).await()
                finalPhotoUrl = ref.downloadUrl.await().toString()
            }

            val data = hashMapOf(
                "name" to name,
                "bio" to bio,
                "email" to user.email
            )
            if (finalPhotoUrl != null) data["photoUrl"] = finalPhotoUrl

            db.collection("users").document(uid).set(data, SetOptions.merge()).await()

            store.saveLocal(uid, name = name, bio = bio, photoUrl = finalPhotoUrl ?: localPhotoUri)
        }
    }
}
