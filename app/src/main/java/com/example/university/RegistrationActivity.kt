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
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.university.database.DBManager
import com.example.university.usefull_stuff.showToast
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
            val mainColor = colorResource(id = R.color.main)

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

                var mColor by remember { mutableStateOf(mainColor) }
                var isHidden by remember { mutableStateOf(true) }
                var pass1 by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = pass1,
                    onValueChange = { pass1 = it; mColor = mainColor },
                    label = { Text("Придумайте пароль") },
                    singleLine = true,
                    visualTransformation = if (isHidden) PasswordVisualTransformation() else VisualTransformation.None,
                    trailingIcon = {
                        val image = if (isHidden)
                            ImageVector.vectorResource(R.drawable.show_pass)
                        else
                            ImageVector.vectorResource(R.drawable.hide_pass)
                        val description = if (isHidden) "Show password" else "Hide password"

                        IconButton(onClick = {isHidden = !isHidden}){
                            Icon(imageVector  = image, description)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Ascii,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = mColor,
                        unfocusedBorderColor = mColor,
                        cursorColor = mColor,
                        focusedLabelColor = Color.Black
                    )
                )

                var isHidden2 by remember { mutableStateOf(true) }
                var mColor2 by remember { mutableStateOf(mainColor) }
                var pass2 by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = pass2,
                    onValueChange = { pass2 = it; mColor2 = mainColor },
                    label = { Text("Повторите пароль") },
                    singleLine = true,
                    visualTransformation = if (isHidden2) PasswordVisualTransformation() else VisualTransformation.None,
                    trailingIcon = {
                        val image = if (isHidden2)
                            ImageVector.vectorResource(R.drawable.show_pass)
                        else
                            ImageVector.vectorResource(R.drawable.hide_pass)
                        val description = if (isHidden2) "Show password" else "Hide password"

                        IconButton(onClick = {isHidden2 = !isHidden2}){
                            Icon(imageVector  = image, description)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Ascii,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            // keyboardController?.hide()
                            if (addUser(pass1, pass2, db, context, sharedPreferences))
                                mColor2 = Color.Red
                            else mColor = Color.Red
                        }),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = mColor2,
                        unfocusedBorderColor = mColor2,
                        cursorColor = mColor2,
                        focusedLabelColor = Color.Black
                    )
                )

                Button(
                    onClick = {
                        if (addUser(pass1, pass2, db, context, sharedPreferences)) {
                            mColor2 = Color.Red
                        }
                        else mColor = Color.Red
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = mainColor,
                        contentColor = Color.White
                    )
                ) {

                    Text(text = "Создать нового пользователя")
                }

                Button(
                    onClick = {
                        toLogin()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = mainColor,
                        contentColor = Color.White
                    )
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
    ): Boolean {
        if (pass1 != pass2) {
            showToast("Пароли не совпадают", context)
            return true
        }
        if (!isValidSymbols(pass1)) {
            showToast("Использованы недопустимые символы", context)
            return false
        }
        if ((pass1 in db.getPasswords().keys) != false) {
            showToast("Пароль не подходит, придумайте другой", context)
            Log.w(
                TAG, "Пароль не подошел, контекст: pass: $pass1, " +
                        "совпадение паролей: ${(pass1 in db.getPasswords().keys)}, " +
                        "Валидность пароля: ${isValidSymbols(pass1)}"
            )
            return false
        }
        db.insertPassword(pass1)
        val newId = db.getPasswords().get(pass1)
        sharedPreferences.edit().putBoolean("session", true).apply()
        newId?.let {
            sharedPreferences.edit().putInt("user", it).apply()
        }
        toMain()

        return false
    }

    private fun isValidSymbols(password: String): Boolean {
        val passwordRegex = Regex("^[a-zA-Z0-9]*$")
        Log.d(
            TAG, "Проверка пароля: " +
                    "валидность символов: ${password.matches(passwordRegex)}, " +
                    "длинна: ${password.length}"
        )
        return (password.matches(passwordRegex) && password.length >= 6)
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