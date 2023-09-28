package com.example.university

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.university.database.DBManager

class LoginActivity : AppCompatActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this /* Activity context */)
        sharedPreferences.edit().putBoolean("session", false).apply()
        val db = DBManager(this)
        super.onCreate(savedInstanceState)

        setContent() {
            val context = LocalContext.current
            val keyboardController = LocalSoftwareKeyboardController.current

            Column(
                Modifier
                    .fillMaxHeight()
                    .padding(50.dp, 0.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Box() {

                    Column(
                        Modifier
                            .fillMaxHeight()
                            .padding(15.dp),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = "Вы не авторизованы",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 20.dp),
                            textAlign = TextAlign.Center,
                            //style = TextStyle(fontWeight = FontWeight.Bold),
                            fontSize = 20.sp,
                        )

                        var pass by remember { mutableStateOf("") }
                        OutlinedTextField(
                            value = pass,
                            onValueChange = { pass = it },
                            label = {
                                Text("Введите пароль")
                            },
                            modifier = Modifier.padding(bottom = 20.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Ascii,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                    checkPassword(pass, db, context, sharedPreferences)
                                })
                        )

                        Button(
                            onClick = {
                                checkPassword(pass, db, context, sharedPreferences)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Text(text = "Войти")
                        }

                        Button(
                            onClick = {
                                toRegistration()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Text(text = "Создать нового пользователя")
                        }
                    }
                }

            }
        }

    }

    private fun checkPassword(
        pass: String,
        db: DBManager,
        context: Context,
        sharedPreferences: SharedPreferences
    ) {
        if (pass.isEmpty()) {
            showToast("Введите пароль", context)
            return
        }
        val passwords = db.getPasswords()
        if (pass in passwords.keys) {
            passwords.get(pass)?.let {
                sharedPreferences.edit().putInt("user", it).apply()
            }
            sharedPreferences.edit().putBoolean("session", true).apply()
            toMain()
        } else {
            showToast("Пароль не верен", context)
        }
    }

    fun showToast(text: String, context: Context) {
        Toast.makeText(
            context,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }

    fun toMain() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun toRegistration() {
        val intent = Intent(this@LoginActivity, RegistrationActivity::class.java)
        startActivity(intent)
    }
}