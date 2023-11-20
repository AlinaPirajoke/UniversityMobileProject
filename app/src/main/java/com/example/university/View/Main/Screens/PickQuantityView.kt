package com.example.university.View.Main.Screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.university.View.Main.MainActivity
import com.example.university.View.Main.MainScreens
import com.example.university.ViewModel.PickQuantityViewModel
import com.example.university.theme.ColorScheme
import com.example.university.theme.KotobaCustomTheme
import org.koin.androidx.compose.koinViewModel

private const val TAG = "PickQuantityView"

@Composable
fun PickQuantityScreen(
    context: MainActivity,
    navController: NavHostController,
    vm: PickQuantityViewModel = koinViewModel()
) {
    val uiState by vm.uiState.collectAsState()

    KotobaCustomTheme(colorScheme = uiState.colorScheme) {
        context.window.statusBarColor = MaterialTheme.colors.primary.toArgb()
        PickQuantityView(pickedQuantity = uiState.pickedQuantity,
            wordsQuantity = uiState.wordsQuantity,
            onValueChange = vm::setQuantity,
            onGoingToTest = {
                Log.i(TAG, "Перенаправление на экран теста")
                navController.navigate(MainScreens.AddNew.route)
            },
            onGoingToMain = {
                Log.i(TAG, "Перенаправление на экран теста")
                navController.navigate(MainScreens.Main.route)
            },
            isRememberPresent = uiState.isRememberPresent,
            onGoingToRemember = {
                Log.i(TAG, "Перенаправление на экран теста")
                navController.navigate(MainScreens.Main.route)
            })
    }
}

@Composable
fun PickQuantityView(
    pickedQuantity: Int,
    wordsQuantity: Int,
    onValueChange: (Int) -> Unit,
    onGoingToTest: () -> Unit,
    onGoingToMain: () -> Unit,
    isRememberPresent: Boolean,
    onGoingToRemember: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween
    ) {
        Card(
            Modifier
                .fillMaxWidth()
                .height(250.dp),
            shape = RoundedCornerShape(0.dp, 0.dp, 30.dp, 30.dp),
            elevation = 4.dp,
            backgroundColor = MaterialTheme.colors.primary,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp, 30.dp),
                verticalArrangement = Arrangement.SpaceAround
            ) {

                Text(
                    text = "Выберите количество слов, которое хотите повторить",
                    fontSize = 20.sp,
                    color = MaterialTheme.colors.onPrimary,
                )

                Text(
                    text = "$pickedQuantity из $wordsQuantity",
                    fontSize = 30.sp,
                    color = MaterialTheme.colors.onPrimary,
                )
                Slider(
                    value = pickedQuantity?.toFloat()!!, onValueChange = {
                        onValueChange(it.toInt())
                    },
                    valueRange = 0f..wordsQuantity?.toFloat()!!,
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colors.primaryVariant,
                        activeTrackColor = MaterialTheme.colors.onPrimary,
                        inactiveTrackColor = MaterialTheme.colors.secondary,
                        inactiveTickColor = MaterialTheme.colors.onPrimary,
                        activeTickColor = MaterialTheme.colors.secondary
                    )
                )
                Row(
                    Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = "Выбрать 5",
                        Modifier.clickable { onValueChange(5) },
                        color = MaterialTheme.colors.onPrimary,
                        fontSize = 20.sp,
                    )
                    Text(
                        text = "Выбрать 10",
                        Modifier.clickable { onValueChange(10) },
                        color = MaterialTheme.colors.onPrimary,
                        fontSize = 20.sp
                    )
                    Text(
                        text = "Выбрать 15",
                        Modifier.clickable { onValueChange(15) },
                        color = MaterialTheme.colors.onPrimary,
                        fontSize = 20.sp
                    )
                }
            }
        }
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            Button(
                onClick = onGoingToTest,
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp, vertical = 30.dp)
                    .height(50.dp)
            ) {
                Text(text = "Пройти тест")
            }
            if (isRememberPresent) Button(
                onClick = onGoingToRemember,
                Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 50.dp)
            ) {
                Text(text = "Только повторить")
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
        ) {
            Button(
                onClick = onGoingToMain,
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.onPrimary,
                    contentColor = MaterialTheme.colors.primary
                )
            ) {
                Text(text = "Домой")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PickQuantityPreview() {
    KotobaCustomTheme(colorScheme = ColorScheme.pink.colors) {
        PickQuantityView(pickedQuantity = 10,
            wordsQuantity = 20,
            onValueChange = { },
            onGoingToTest = { },
            onGoingToMain = { },
            isRememberPresent = true,
            onGoingToRemember = { })
    }
}