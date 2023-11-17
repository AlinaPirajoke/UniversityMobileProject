package com.example.university.View.Auth.Screens

import android.util.Log
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import com.example.university.R
import com.example.university.View.Auth.AuthActivity
import com.example.university.View.Auth.AuthScreens
import com.example.university.ViewModel.RegistrationViewModel
import com.example.university.ViewModel.RegistrationViewModelFactory
import com.example.university.usefull_stuff.showToast

@Composable
fun registrationInit(context: AuthActivity, navController: NavHostController) {
    val vm = ViewModelProvider(
        context,
        RegistrationViewModelFactory(context)
    ).get(RegistrationViewModel::class.java)
    registrationScreen(context = context, navController = navController, vm = vm)
}

@Composable
fun registrationScreen(
    context: AuthActivity, navController: NavHostController, vm: RegistrationViewModel
) {
    // Мня за такие аргументы не опустят?


    val uiState by vm.uiState.collectAsState()

    if (uiState.isGoingToMain) context.toMain()
    if (uiState.isGoingToLogin) {
        Log.i("registrationView", "Перенаправление на логин: ${uiState.isGoingToLogin}")
        vm.sendToLoginPage(false)
        navController.navigate(AuthScreens.Login.route)
    }
    if (!uiState.errorMessage.isEmpty()) {
        showToast(uiState.errorMessage, context)
        vm.clearErrorMessage()
        Log.w("registrationView", "Получена ошибка: ${uiState.errorMessage}")
    }

    registrationView(
        pass1 = vm.enteredPass1,
        pass2 = vm.enteredPass2,
        onUserInput1Changed = { vm.setPass1Value(it) },
        onUserInput2Changed = { vm.setPass2Value(it) },
        onPassConfirm = { vm.addUser() },
        onGoingToLogin = { vm.sendToLoginPage() },
        isField1Error = uiState.isField1Wrong,
        isField2Error = uiState.isField2Wrong,
    )
}

@Composable
fun registrationView(
    pass1: String,
    pass2: String,
    onUserInput1Changed: (String) -> Unit,
    onUserInput2Changed: (String) -> Unit,
    onPassConfirm: () -> Unit,
    onGoingToLogin: () -> Unit,
    isField1Error: Boolean,
    isField2Error: Boolean,
) {

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
        var isHidden1 by remember { mutableStateOf(true) }

        OutlinedTextField(value = pass1,
            onValueChange = { onUserInput1Changed(it) },
            label = {
                if (isField1Error) Text("Такой пароль не подойдёт")
                else Text("Придумайте пароль")
            },
            singleLine = true,
            visualTransformation = if (isHidden1) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = {
                val image = if (isHidden1) ImageVector.vectorResource(R.drawable.show_pass)
                else ImageVector.vectorResource(R.drawable.hide_pass)
                val description = if (isHidden1) "Show password" else "Hide password"

                IconButton(onClick = { isHidden1 = !isHidden1 }) {
                    Icon(imageVector = image, description)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            isError = isField1Error,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Ascii, imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.primary,
                unfocusedBorderColor = MaterialTheme.colors.primary,
                cursorColor = MaterialTheme.colors.primary,
                focusedLabelColor = MaterialTheme.colors.secondary,
            )
        )

        var isHidden2 by remember { mutableStateOf(true) }
        OutlinedTextField(value = pass2,
            onValueChange = { onUserInput2Changed(it) },
            label = {
                if (isField2Error) Text("Такой пароль не подойдёт")
                else Text("Повторите пароль")
            },
            singleLine = true,
            visualTransformation = if (isHidden2) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = {
                val image = if (isHidden2) ImageVector.vectorResource(R.drawable.show_pass)
                else ImageVector.vectorResource(R.drawable.hide_pass)
                val description = if (isHidden2) "Show password" else "Hide password"

                IconButton(onClick = { isHidden2 = !isHidden2 }) {
                    Icon(imageVector = image, description)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            isError = isField2Error,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Ascii, imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                // keyboardController?.hide()
                onPassConfirm()
            }),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.primary,
                unfocusedBorderColor = MaterialTheme.colors.primary,
                cursorColor = MaterialTheme.colors.primary,
                focusedLabelColor = MaterialTheme.colors.secondary,
            )
        )

        Button(
            onClick = {
                onPassConfirm()
            }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary,
            )
        ) {

            Text(text = "Создать нового пользователя")
        }

        Button(
            onClick = {
                onGoingToLogin()
            }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.onPrimary,
                contentColor = MaterialTheme.colors.primary
            )
        ) {

            Text(text = "Войти")
        }
    }

}