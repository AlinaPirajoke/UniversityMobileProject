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
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.university.database.DBManager

class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = DBManager(this)
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this /* Activity context */)

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
                    Text(
                        text = "Создайте нового пользователя",
                        modifier = Modifier.padding(bottom = 20.dp),
                    )

                    var pass1 by remember { mutableStateOf("1") }
                    OutlinedTextField(value = pass1,
                        onValueChange = { pass1 = it },
                        label = { Text("Придумайте пароль") }
                    )

                    var pass2 by remember { mutableStateOf("2") }
                    OutlinedTextField(value = pass2,
                        onValueChange = { pass2 = it },
                        label = { Text("Повторите пароль") },
                        modifier = Modifier.padding(bottom = 20.dp),
                    )

                    Button(
                        onClick = {
                            if (pass1 == pass2) {
                                if (!(pass1 in db.getPasswords())) {
                                    db.insertPassword(pass1)
                                    val newId = db.getPasswords().get(pass1)
                                    sharedPreferences.edit().putBoolean("session", true).apply()
                                    newId?.let {
                                        sharedPreferences.edit().putInt("user", it).apply()
                                    }
                                    toMain()
                                } else {
                                    //("Пароль существует")
                                }
                            } else {
                                //("Пароли разные")
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Text(text = "Создать нового пользователя")
                    }

                    Button(
                        onClick = {
                            toLogin()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Text(text = "Войти")
                    }
                }
            }
        }
    }


    fun toLogin() {
        fun toLogin() {
            val intent = Intent(this@RegistrationActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    fun toMain() {
        val intent = Intent(this@RegistrationActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}