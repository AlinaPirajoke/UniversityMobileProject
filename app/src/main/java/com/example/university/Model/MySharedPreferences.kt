package com.example.university.Model

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import androidx.compose.material.Colors
import com.example.university.theme.ColorScheme

class MySharedPreferences(context: Context) {
    val TAG = "MySharedPreferences"
    val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    var session: Boolean = false
        get(){
            val v = sp.getBoolean("session", false)
            Log.i(TAG, "session: $v")
            return v
        }
        set(condition: Boolean){
            field = condition
            sp.edit().putBoolean("session", condition).apply()
        }

    var user: Int = 0
        get() {
            val v = sp.getInt("user", 0)
            Log.i(TAG, "user: $v")
            return v
        }
        set(id: Int){
            field = id
            sp.edit().putInt("user", id).apply()
        }

    var isPasswordNeeded: Boolean = true
        get() {
            val v = sp.getBoolean("isPasswordNeeded", true)
            Log.i(TAG, "isPasswordNeeded: $v")
            return v
        }
        set(condition: Boolean){
            field = condition
            sp.edit().putBoolean("isPasswordNeeded", condition).apply()
        }

    var currentColorSchemeId: Int = 0
        get() {
            val v = sp.getInt("currentColorSchemeId", 0)
            Log.i(TAG, "currentColorSchemeId: $v")
            return v
        }
        set(id: Int) {
            field = id
            sp.edit().putInt("currentColorSchemeId", id).apply()
        }

    var studyQuantityPerDay: Int = 999
        get() {
            val v = sp.getInt("studyQuantityPerDay", 10)
            Log.i(TAG, "studyQuantityPerDay: $v")
            return v
        }
        set(count: Int) {
            field = count
            sp.edit().putInt("studyQuantityPerDay", count).apply()
        }

    var isRememberPresent: Boolean = false
        get() {
            val v = sp.getBoolean("isRememberPresent", false)
            Log.i(TAG, "isRememberPresent: $v")
            return v
        }
        set(condition: Boolean){
            field = condition
            sp.edit().putBoolean("isRememberPresent", condition).apply()
        }

    fun getColorScheme(): Colors {
        val v = sp.getInt("currentColorSchemeId", 0)
        Log.i(TAG, "currentColorSchemeId: $v")
        return when (v) {
            0 -> ColorScheme.PH.colors
            1 -> ColorScheme.pink.colors
            else -> ColorScheme.PH.colors
        }
    }
}