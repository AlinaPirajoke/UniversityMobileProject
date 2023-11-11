package com.example.university.Model

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.compose.material.Colors
import com.example.university.theme.ColorScheme

class MySharedPreferences(context: Context) {
    val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    var session: Boolean = false
        get() = sp.getBoolean("session", false)
        set(condition: Boolean){
            field = condition
            sp.edit().putBoolean("session", condition).apply()
        }

    fun getColorScheme(): Colors {
        return when (sp.getInt("currentColorScheme", 0)) {
            0 -> ColorScheme.PH.colors
            1 -> ColorScheme.pink.colors
            else -> ColorScheme.PH.colors
        }
    }

    fun setColorScheme(id: Int) = sp.edit().putInt("currentColorScheme", id).apply()

}