package com.example.university

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.university.database.DBManager

class RegistrationActivity : AppCompatActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = DBManager(this)
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this /* Activity context */)

        setContent() {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            val context = LocalContext.current
            val keyboardController = LocalSoftwareKeyboardController.current
            val focusManager = LocalFocusManager.current
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

                    var pass1 by remember { mutableStateOf("") }
                    OutlinedTextField(value = pass1,
                        onValueChange = { pass1 = it },
                        label = { Text("Придумайте пароль") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii,
                            imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {keyboardController?.hide()})
                    )

                    var pass2 by remember { mutableStateOf("") }
                    OutlinedTextField(
                        value = pass2,
                        onValueChange = { pass2 = it },
                        label = { Text("Повторите пароль") },
                        modifier = Modifier.padding(bottom = 20.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Ascii,
                            imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }),

                    )

                    Button(
                        onClick = {
                            if (pass1 == pass2) {
                                if ((pass1 in db.getPasswords().keys) == false && isValidPassword(pass1) ) {
                                    db.insertPassword(pass1)
                                    val newId = db.getPasswords().get(pass1)
                                    sharedPreferences.edit().putBoolean("session", true).apply()
                                    newId?.let {
                                        sharedPreferences.edit().putInt("user", it).apply()
                                    }
                                    toMain()
                                } else {
                                    showToast("Пароль не подходит, придумайте другой", context)
                                }
                            } else {
                                showToast("Пароли не совпадают", context)
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

    fun showToast(text: String, context: Context) {
        Toast.makeText(
            context,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}\$"
        return (password.matches(passwordRegex.toRegex()) && password.length > 6)
    }

    fun toLogin() {
        val intent = Intent(this@RegistrationActivity, LoginActivity::class.java)
        startActivity(intent)
    }

    fun toMain() {
        val intent = Intent(this@RegistrationActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}