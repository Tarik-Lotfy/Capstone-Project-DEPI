package com.example.moviestime.data.repository

import android.util.Log
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await // نستخدم هذه المكتبة الجاهزة بدلاً من كتابة دالة يدوية

class AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val currentUser: FirebaseUser? get() = auth.currentUser

    suspend fun login(email: String, password: String): Result<FirebaseUser> {
        return try {
            // نستخدم await() القادمة من المكتبة مباشرة
            val result = auth.signInWithEmailAndPassword(email, password).await()
            if (result.user != null) {
                Result.success(result.user!!)
            } else {
                Result.failure(Exception("User is null"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Login failed", e)
            Result.failure(e)
        }
    }

    suspend fun register(email: String, password: String, displayName: String): Result<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user!!

            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build()

            user.updateProfile(profileUpdates).await()

            Result.success(user)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Registration failed", e)
            Result.failure(e)
        }
    }

    suspend fun signInWithGoogleToken(idToken: String): Result<FirebaseUser> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(credential).await()
            if (result.user != null) {
                Result.success(result.user!!)
            } else {
                Result.failure(Exception("Google User is null"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Google Sign-In failed", e)
            Result.failure(e)
        }
    }

    suspend fun signInWithFacebookToken(token: String): Result<FirebaseUser> {
        return try {
            val credential = FacebookAuthProvider.getCredential(token)
            val result = auth.signInWithCredential(credential).await()
            if (result.user != null) {
                Result.success(result.user!!)
            } else {
                Result.failure(Exception("Facebook User is null"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Facebook Sign-In failed", e)
            Result.failure(e)
        }
    }

    fun logout() {
        auth.signOut()
    }

    // تم حذف الدالة اليدوية await() لتجنب الأخطاء
}