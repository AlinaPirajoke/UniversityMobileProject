package com.example.university

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.university.ViewModel.LoginViewModel
import com.example.university.ViewModel.LoginViewModelFactory
import com.example.university.database.DBManager
import com.example.university.theme.mainColor
import com.example.university.usefull_stuff.showToast

// private val Context.dataStore by preferencesDataStore("user_preferences")

class LoginActivity : AppCompatActivity() {

    @OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this /* Activity context */)
        sharedPreferences.edit().putBoolean("session", false).apply()
        super.onCreate(savedInstanceState)
        val vm = ViewModelProvider(this, LoginViewModelFactory(this)).get(LoginViewModel::class.java)

        vm.isGoingToMain.observe(this, Observer {
            if(it) toMain()
        })
        vm.errorMesage.observe(this, Observer {
            showToast(it, this)
        })



        setContent() {
            //val mainColor = colorResource(id = R.color.main)
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
                        var mColor by remember { mutableStateOf(mainColor) }
                        vm.fieldColor.observe(LocalLifecycleOwner.current, Observer {
                            mColor = it
                        })
                        var isHidden by remember { mutableStateOf(true) }

                        OutlinedTextField(
                            value = pass,
                            onValueChange = { pass = it; vm.setNormalFieldColor() },
                            label = { Text("Введите пароль") },
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
                            modifier = Modifier . padding (bottom = 20.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Ascii,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    // keyboardController?.hide()
                                    vm.checkPassword(pass, sharedPreferences)
                                }),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = mColor,
                                unfocusedBorderColor = mColor,
                                cursorColor = mColor,
                                focusedLabelColor = Color.Black
                            )
                        )

                        Button(
                            onClick = {
                                vm.checkPassword(pass, sharedPreferences)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = mainColor,
                                contentColor = Color.White
                            )
                        ) {

                            Text(text = "Войти")
                        }

                        Button(
                            onClick = {
                                toRegistration()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = mainColor,
                                contentColor = Color.White
                            )
                        ) {

                            Text(text = "Создать нового пользователя")
                        }
                    }
                }

            }
        }

    }

    fun getDataStore(context: Context){

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