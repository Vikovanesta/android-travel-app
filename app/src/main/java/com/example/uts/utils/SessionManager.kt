package com.example.uts.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences

    companion object {
        private const val PREFS_FILENAME = "Auth"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_USER_ID = "userId"
        private const val KEY_USER_ROLE = "userRole"
        private const val KEY_USERNAME = "username"
        private const val KEY_USER_EMAIL = "userEmail"
        private const val KEY_USER_NIM = "userNim"

        @Volatile
        private var INSTANCE: SessionManager? = null

        fun getInstance(context: Context): SessionManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SessionManager(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }
    }

    init {
        sharedPreferences = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun setUserId(userId: String) {
        sharedPreferences.edit().putString(KEY_USER_ID, userId).apply()
    }

    fun getUserId(): String {
        return sharedPreferences.getString(KEY_USER_ID, "")!!
    }

    fun setUserRole(userRole: String) {
        sharedPreferences.edit().putString(KEY_USER_ROLE, userRole).apply()
    }

    fun getUserRole(): String {
        return sharedPreferences.getString(KEY_USER_ROLE, "")!!
    }

    fun setUsername(username: String) {
        sharedPreferences.edit().putString(KEY_USERNAME, username).apply()
    }

    fun getUsername(): String {
        return sharedPreferences.getString(KEY_USERNAME, "")!!
    }

    fun setUserEmail(userEmail: String) {
        sharedPreferences.edit().putString(KEY_USER_EMAIL, userEmail).apply()
    }

    fun getUserEmail(): String {
        return sharedPreferences.getString(KEY_USER_EMAIL, "")!!
    }

    fun setUserNim(userNim: String) {
        sharedPreferences.edit().putString(KEY_USER_NIM, userNim).apply()
    }

    fun getUserNim(): String {
        return sharedPreferences.getString(KEY_USER_NIM, "")!!
    }

    fun logout() {
        sharedPreferences.edit().clear().apply()
    }
}