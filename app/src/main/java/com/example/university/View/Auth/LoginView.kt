package com.example.university.View.Auth

import android.util.Log
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.university.ViewModel.LoginViewModel
import com.example.university.ViewModel.LoginViewModelFactory
import com.example.university.ViewModel.RegistrationViewModel
import com.example.university.ViewModel.RegistrationViewModelFactory
import com.example.university.usefull_stuff.showToast


@Composable
fun loginInit(context: AuthActivity, navController: NavHostController) {
    val vm = ViewModelProvider(context, LoginViewModelFactory(context))
        .get(LoginViewModel::class.java)
    loginScreen(context = context, navController = navController, vm = vm)
}

@Composable
fun loginScreen(context: AuthActivity, navController: NavHostController, vm: LoginViewModel) {
    // Такие аргументы использовать не зашкварно?
    // Нигде не видел, чтобы так делали, но как иначе не придумал

    val uiState by vm.uiState.collectAsState()

    if(uiState.isGoingToMain)
        context.toMain()
    if(uiState.isGoingToRegister) {
        Log.i("LoginView", "Перенаправление на регистрацию: ${uiState.isGoingToRegister}")
        vm.sendToRegisterPage(false)
        navController.navigate(AuthScreens.Registration.route)
    }
    if (!uiState.errorMessage.isEmpty()) {
        showToast(uiState.errorMessage, context)
        vm.clearErrorMessage()
        Log.w("LoginView", "Получена ошибка: ${uiState.errorMessage}")
    }

    loginView(
        pass = vm.enteredPass,
        onUserInputChanged = { vm.editUserEnter(it) },
        onPassConfirm = { vm.checkPassword() },
        onGoingToRegister = { vm.sendToRegisterPage() },
        isError = uiState.isFieldWrong,
    )
}

@Composable
fun loginView(
    pass: String,
    onUserInputChanged: (String) -> Unit,
    onPassConfirm: () -> Unit,
    onGoingToRegister: () -> Unit,
    isError: Boolean,
) {
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

                var isHidden by remember { mutableStateOf(true) }

                OutlinedTextField(
                    value = pass,
                    onValueChange = { onUserInputChanged(it) },
                    label = {
                        if (isError)
                            Text("Пароль не верен")
                        else
                            Text("Введите пароль")
                    },
                    singleLine = true,
                    visualTransformation = if (isHidden) PasswordVisualTransformation() else VisualTransformation.None,
                    trailingIcon = {
                        val image = if (isHidden)
                            ImageVector.vectorResource(R.drawable.show_pass)
                        else
                            ImageVector.vectorResource(R.drawable.hide_pass)
                        val description = if (isHidden) "Show password" else "Hide password"

                        IconButton(onClick = { isHidden = !isHidden }) {
                            Icon(imageVector = image, description)
                        }
                    },
                    modifier = Modifier.padding(bottom = 20.dp),
                    isError = isError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Ascii,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
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
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primary,
                        contentColor = Color.White
                    )
                ) {

                    Text(text = "Войти")
                }

                Button(
                    onClick = {
                        onGoingToRegister()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primary,
                        contentColor = MaterialTheme.colors.onPrimary
                    )
                ) {

                    Text(text = "Создать нового пользователя")
                }
            }
        }

    }
}
