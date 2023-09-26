package com.example.university

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this /* Activity context */)
        //sharedPreferences.edit().putBoolean("session", false).apply()

        val session = sharedPreferences.getBoolean("session", false)
        if (!session) {
            toLogin()
            finish()
        }
        val user = sharedPreferences.getInt("user", 0)

        super.onCreate(savedInstanceState)
        setContent() {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalArrangement = Arrangement.Center
            ) {

                Column(
                    Modifier
                        .fillMaxHeight()
                        .padding(15.dp),
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(text = "Статус session: ${ session }")
                    Text(text = "Привет, человек номер${user}")
                    Button(onClick = { toLogin() }) {
                        Text(text = "Разлогиниться")
                    }
                }
            }
        }
    }

    fun toLogin() {
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}