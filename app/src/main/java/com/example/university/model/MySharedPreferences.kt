package com.example.university.model

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import androidx.compose.material.Colors
import com.example.university.theme.ColorScheme
import com.example.university.usefullStuff.getTodayDate

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
            Log.i(TAG, "session: $condition")
        }

    var user: Int = 0
        get() {
            val v = sp.getInt("user", 1)
            Log.i(TAG, "user: $v")
            return v
        }
        set(id: Int){
            field = id
            sp.edit().putInt("user", id).apply()
            Log.i(TAG, "user: $id")
        }

    var isPasswordNeeded: Boolean = false
        get() {
            val v = sp.getBoolean("isPasswordNeeded", false)
            Log.i(TAG, "isPasswordNeeded: $v")
            return v
        }
        set(condition: Boolean){
            field = condition
            sp.edit().putBoolean("isPasswordNeeded", condition).apply()
            Log.i(TAG, "isPasswordNeeded: $condition")
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
            Log.i(TAG, "currentColorSchemeId: $id")
        }

    var studyQuantityPerDay: Int = 999
        get() {
            val v = sp.getInt("studyQuantityPerDay", 10)
            Log.i(TAG, "studyQuantityPerDay: $v")
            return v
        }
        set(quantity: Int) {
            field = quantity
            sp.edit().putInt("studyQuantityPerDay", quantity).apply()
            Log.i(TAG, "studyQuantityPerDay: $quantity")
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
            Log.i(TAG, "isRememberPresent: $condition")
        }

    var lastOpenedAppDate: String = "1970-01-01"
        get() {
            val v = sp.getString("lastOpenedAppDate", getTodayDate())
            Log.i(TAG, "lastOpenedAppDate: $v")
            return v.toString()
        }
        set(date: String){
            field = date
            sp.edit().putString("lastOpenedAppDate", date).apply()
            Log.i(TAG, "lastOpenedAppDate: $date")
        }

    var todayStudiedQuantity: Int = 999
        get() {
            val v = sp.getInt("todayStudiedQuantity", 0)
            Log.i(TAG, "todayStudiedQuantity: $v")
            return v
        }
        set(quantity: Int) {
            field = quantity
            sp.edit().putInt("todayStudiedQuantity", quantity).apply()
            Log.i(TAG, "todayStudiedQuantity: $quantity")
        }

    var firstAppAccessDate: String = ""
        get() {
            val v = sp.getString("firstAppAccessDate", "")
            Log.i(TAG, "firstAppAccessDate: $v")
            return v.toString()
        }
        set(date: String){
            field = date
            sp.edit().putString("firstAppAccessDate", date).apply()
            Log.i(TAG, "firstAppAccessDate: $date")
        }

    var isAnimationsLong: Boolean = false
        get() {
            val v = sp.getBoolean("isAnimationsLong", false)
            Log.i(TAG, "isAnimationsLong: $v")
            return v
        }
        set(condition: Boolean){
            field = condition
            sp.edit().putBoolean("isAnimationsLong", condition).apply()
            Log.i(TAG, "isAnimationsLong: $condition")
        }

    fun getColorScheme(): Colors {
        val v = sp.getInt("currentColorSchemeId", 0)
        Log.i(TAG, "currentColorSchemeId: $v")
//        return ColorScheme.values()[v].colors
        return when (v) {
            0 -> ColorScheme.PH.colors
            1 -> ColorScheme.pink.colors
            2 -> ColorScheme.mint.colors
            else -> ColorScheme.PH.colors
        }
    }
}