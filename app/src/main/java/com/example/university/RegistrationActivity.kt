package com.example.university

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.university.database.DBManager
import kotlinx.coroutines.launch

class RegistrationActivity : AppCompatActivity() {
    val TAG = "RegistrationActivity"

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = DBManager(this)
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this /* Activity context */)

        setContent() {
            var parentSize by remember {
                mutableStateOf(Size.Zero)
            }
            val context = LocalContext.current
            val keyboardController = LocalSoftwareKeyboardController.current
            val focusManager = LocalFocusManager.current


            val scrollState = rememberScrollState()
            Column(
                Modifier
                    .fillMaxHeight()
                    .padding(50.dp, 0.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Center


            ) {
                Text(
                    text = "Создайте нового пользователя",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    textAlign = TextAlign.Center,
                    //style = TextStyle(fontWeight = FontWeight.Bold),
                    fontSize = 20.sp,
                )

                var pass1 by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = pass1,
                    onValueChange = { pass1 = it },
                    label = { Text("Придумайте пароль") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Ascii,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }),
                    modifier = Modifier
                        .fillMaxWidth()
                )

                var pass2 by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = pass2,
                    onValueChange = { pass2 = it },
                    label = { Text("Повторите пароль") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Ascii,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            addUser(pass1, pass2, db, context, sharedPreferences)
                        })
                )

                Button(
                    onClick = {
                        addUser(pass1, pass2, db, context, sharedPreferences)
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

    fun addUser(
        pass1: String,
        pass2: String,
        db: DBManager,
        context: Context,
        sharedPreferences: SharedPreferences
    ) {
        if (pass1 == pass2) {
            if ((pass1 in db.getPasswords().keys) == false && isValidSymbols(pass1)) {
                db.insertPassword(pass1)
                val newId = db.getPasswords().get(pass1)
                sharedPreferences.edit().putBoolean("session", true).apply()
                newId?.let {
                    sharedPreferences.edit().putInt("user", it).apply()
                }
                toMain()
            } else {
                showToast("Пароль не подходит, придумайте другой", context)
                Log.w(TAG, "Пароль не подошел, контекст: pass: $pass1, " +
                            "совпадение паролей: ${(pass1 in db.getPasswords().keys)}, " +
                            "Валидность пароля: ${isValidSymbols(pass1)}")
            }
        } else {
            showToast("Пароли не совпадают", context)
        }
    }

    private fun isValidSymbols(password: String): Boolean {
        val passwordRegex = Regex("^[a-zA-Z0-9]*$")
        Log.d(TAG, "Проверка пароля: " +
                "валидность символов: ${password.matches(passwordRegex)}, " +
                "длинна: ${password.length}")
        return (password.matches(passwordRegex) && password.length >= 6)
    }

    fun showToast(text: String, context: Context) {
        Toast.makeText(
            context,
            text,
            Toast.LENGTH_SHORT
        ).show()
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