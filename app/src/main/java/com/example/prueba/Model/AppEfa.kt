package com.example.prueba.Model

import android.app.Application
import android.content.Context
import android.util.Log

class AppEfa : Application() {

    companion object {
        lateinit var preferences: Preferences
        private const val TAG = "AppEfa"
    }

    override fun onCreate() {
        super.onCreate()


        preferences = Preferences(applicationContext)
        Log.i(TAG, "Preferences initialized")
    }

    class Preferences(context: Context) {

        private val storage = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        fun saveToken(token: String) {
            storage.edit().putString("token", token).apply()
        }

        fun getToken(): String? {
            return storage.getString("token", null)  // Devuelve null si no existe
        }

        fun clearPreferences() {
            storage.edit().clear().apply()
        }
    }
}
