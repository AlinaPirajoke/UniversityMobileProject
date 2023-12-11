package com.example.university.View.Main.Screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.university.View.Main.MainScreens
import com.example.university.ViewModel.SettingsViewModel
import com.example.university.theme.PH
import com.example.university.theme.pink
import org.koin.androidx.compose.koinViewModel

private val TAG = "SettingsView"

@Composable
fun SettingsScreen(
    onChangeColorScheme: () -> Unit,
    navController: NavHostController,
    vm: SettingsViewModel = koinViewModel(),
    showErrorMessage: (String) -> Unit,
) {
    val uiState by vm.uiState.collectAsState()
    if (uiState.haveErrorMessage) {
        showErrorMessage(uiState.errorMessage)
        vm.setHaveErrorMessage(false)
    }
    SettingsView(
        isPassNeeded = uiState.isPasswordNeeded,
        onIsPassNeededChange = {
            vm.setIsPasswordNeeded(it)
        },
        isRememberPresent = uiState.isRememberPresent,
        onIsRememberPresentChange = vm::setIsRememberPresent,
        currentColorScheme = uiState.currentColorScheme,
        onColorSchemeChange = {
            vm.setCurrentColorScheme(it)
            onChangeColorScheme()
        },
        onGoingToMain = {
            Log.i(TAG, "Перенаправление на главный экран")
            navController.navigate(MainScreens.Main.route)
        }
    )
}

@Composable
fun SettingsView(
    isPassNeeded: Boolean,
    onIsPassNeededChange: (Boolean) -> Unit,
    isRememberPresent: Boolean,
    onIsRememberPresentChange: (Boolean) -> Unit,
    currentColorScheme: Int,
    onColorSchemeChange: (Int) -> Unit,
    onGoingToMain: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp, 10.dp),
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Настройки", fontSize = 20.sp)

            Button(
                onClick = onGoingToMain,
                modifier = Modifier.width(125.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary
                )
            ) {
                Text(text = "Выйти")
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(15.dp)
        ) {
            item {
                SwitchOption(
                    currentState = isPassNeeded,
                    description = "Вход через пароль",
                    action = onIsPassNeededChange,
                )
            }
            item {
                SwitchOption(
                    currentState = isRememberPresent,
                    description = "Предлогать повторить пройденные слова",
                    action = onIsRememberPresentChange
                )
            }
            item {
                Text(
                    text = "Цветовая схема приложения",
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h5
                )
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    // PH тема
                    colorTile(
                        bcColor = PH,
                        onClick = { onColorSchemeChange(0) },
                        isPicked = currentColorScheme == 0
                    )
                    // pink тема
                    colorTile(
                        bcColor = pink,
                        onClick = { onColorSchemeChange(1) },
                        isPicked = currentColorScheme == 1
                    )
                }
            }
        }
    }
}

@Composable
private fun SwitchOption(
    currentState: Boolean,
    description: String,
    action: (Boolean) -> (Unit),
) {
    // var checked by remember { mutableStateOf(currentState) }

    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = description,
            Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.subtitle1,
            textAlign = TextAlign.Left
        )

        Switch(
            checked = currentState,
            onCheckedChange = {
                Log.d(TAG, "Попытка установить настройку \"$description\" в состояние $it")
                action(it)
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colors.primary,
                checkedTrackColor = MaterialTheme.colors.primaryVariant,
                uncheckedThumbColor = MaterialTheme.colors.secondary,
                uncheckedTrackColor = MaterialTheme.colors.secondaryVariant
            ),
            modifier = Modifier.padding(top = 10.dp)
        )


    }
}

@Composable
fun colorTile(
    bcColor: Color,
    onClick: () -> (Unit),
    isPicked: Boolean = false
) {
    //Log.i(TAG, isPicked.toString())
    Surface(
        Modifier
            .size(width = 60.dp, height = 60.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        color = bcColor,
        elevation = if (isPicked) 6.dp else 0.dp
    ) {}
}