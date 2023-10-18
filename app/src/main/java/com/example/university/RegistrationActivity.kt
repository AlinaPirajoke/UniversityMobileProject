package com.example.university

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.university.ViewModel.RegistrationViewModel
import com.example.university.ViewModel.RegistrationViewModelFactory
import com.example.university.database.DBManager
import com.example.university.usefull_stuff.showToast

class RegistrationActivity : AppCompatActivity() {
    val TAG = "RegistrationActivity"


    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val vm = ViewModelProvider(this, RegistrationViewModelFactory(this))
            .get(RegistrationViewModel::class.java)

        vm.isGoingToMain.observe(this, Observer {
            if(it) toMain()
        })
        vm.errorMessage.observe(this, Observer {
            showToast(it, this)
        })

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
                var isHidden1 by remember { mutableStateOf(true) }
                var mColor1 by remember { mutableStateOf(mainColor) }
                vm.field1Color.observe(LocalLifecycleOwner.current, Observer {
                    mColor1 = it
                })
                var pass1 by remember { mutableStateOf("") }
                vm.pass1.observe(LocalLifecycleOwner.current, Observer {
                    pass1 = it
                })
                OutlinedTextField(
                    value = pass1,
                    onValueChange = { vm.setPass1Value(it) },
                    label = { Text("Придумайте пароль") },
                    singleLine = true,
                    visualTransformation = if (isHidden1) PasswordVisualTransformation() else VisualTransformation.None,
                    trailingIcon = {
                        val image = if (isHidden1)
                            ImageVector.vectorResource(R.drawable.show_pass)
                        else
                            ImageVector.vectorResource(R.drawable.hide_pass)
                        val description = if (isHidden1) "Show password" else "Hide password"

                        IconButton(onClick = {isHidden1 = !isHidden1}){
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
                        focusedBorderColor = mColor1,
                        unfocusedBorderColor = mColor1,
                        cursorColor = mColor1,
                        focusedLabelColor = Color.Black
                    )
                )

                var isHidden2 by remember { mutableStateOf(true) }
                var mColor2 by remember { mutableStateOf(mainColor) }
                vm.field2Color.observe(LocalLifecycleOwner.current, Observer {
                    mColor2 = it
                })
                var pass2 by remember { mutableStateOf("") }
                vm.pass2.observe(LocalLifecycleOwner.current, Observer {
                    pass2 = it
                })
                OutlinedTextField(
                    value = pass2,
                    onValueChange = { vm.setPass2Value(it) },
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
                            vm.addUser()
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
                        vm.addUser()
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