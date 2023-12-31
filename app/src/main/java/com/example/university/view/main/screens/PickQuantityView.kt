package com.example.university.view.main.screens

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.university.R
import com.example.university.view.main.MainScreens
import com.example.university.viewModel.PickQuantityViewModel
import com.example.university.theme.ColorScheme
import com.example.university.theme.KotobaCustomTheme
import com.example.university.theme.UXConstants
import org.koin.androidx.compose.koinViewModel

private const val TAG = "PickQuantityView"

@Composable
fun PickQuantityInit(
    navController: NavHostController,
    date: String,
    vm: PickQuantityViewModel = koinViewModel()
) {
    Log.i(TAG, "Выбранная дата: $date")
    vm.setDate(date)
    PickQuantityScreen(navController = navController, vm = vm)
}

@Composable
fun PickQuantityScreen(
    navController: NavHostController,
    vm: PickQuantityViewModel,
) {
    val uiState by vm.uiState.collectAsState()

    PickQuantityView(
        pickedQuantitySlider = uiState.pickedQuantitySlider,
        pickedQuantityReal = uiState.pickedQuantityReal,
        wordsQuantity = uiState.wordsQuantity,
        onValueChange = vm::setPickedQuantity,
        onGoingToTest = {
            Log.i(TAG, "Перенаправление на экран теста")
            val listId = vm.createList()
            navController.navigate("${MainScreens.Test.route}/${listId}")
        },
        onGoingToMain = {
            Log.i(TAG, "Перенаправление на главный экран")
            navController.navigate(MainScreens.Main.route)
        },
        isRememberPresent = uiState.isRememberPresent,
        onGoingToRemember = {
            Log.i(TAG, "Перенаправление на экран повторения")
            val listId = vm.createList()
            navController.navigate("${MainScreens.Remember.route}/${listId}")
        },
        pickedWords = uiState.pickedWords
    )
}

@Composable
fun PickQuantityView(
    pickedQuantitySlider: Float,
    pickedQuantityReal: Int,
    wordsQuantity: Int,
    onValueChange: (Float) -> Unit,
    onGoingToTest: () -> Unit,
    onGoingToMain: () -> Unit,
    isRememberPresent: Boolean,
    onGoingToRemember: () -> Unit,
    pickedWords: String,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {

            Card(
                Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                shape = RoundedCornerShape(0.dp, 0.dp, 20.dp, 20.dp),
                elevation = 4.dp,
                backgroundColor = MaterialTheme.colors.primary,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp, 5.dp, 20.dp, 20.dp),
                    verticalArrangement = Arrangement.SpaceAround
                ) {

                    Text(
                        text = "Выберите количество слов, которое хотите повторить",
                        Modifier.padding(bottom = UXConstants.VERTICAL_PADDING),
                        color = MaterialTheme.colors.onPrimary,
                        style = MaterialTheme.typography.h5,
                    )

                    Text(
                        text = "$pickedQuantityReal из $wordsQuantity",
                        fontWeight = FontWeight.W600,
                        fontSize = 20.sp,
                        color = MaterialTheme.colors.onPrimary,
                    )
                    Slider(
                        value = pickedQuantitySlider, onValueChange = {
                            onValueChange(it)
                            Log.i(TAG, "$it")
                        },
                        valueRange = 1f..wordsQuantity.toFloat(),
                        colors = SliderDefaults.colors(
                            thumbColor = MaterialTheme.colors.primaryVariant,
                            activeTrackColor = MaterialTheme.colors.onPrimary,
                            inactiveTrackColor = MaterialTheme.colors.secondary,
                            inactiveTickColor = MaterialTheme.colors.onPrimary,
                            activeTickColor = MaterialTheme.colors.onPrimary,
                        )
                    )
                    Row(
                        Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(
                            text = "Выбрать 5",
                            Modifier.clickable { onValueChange(5f) },
                            color = MaterialTheme.colors.onPrimary,
                            fontSize = 20.sp,
                        )
                        Text(
                            text = "Выбрать 15",
                            Modifier.clickable { onValueChange(10f) },
                            color = MaterialTheme.colors.onPrimary,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "Выбрать всё",
                            Modifier.clickable { onValueChange(1000f) },
                            color = MaterialTheme.colors.onPrimary,
                            fontSize = 20.sp
                        )
                    }
                }
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                Text(
                    text = "Выбранные слова:",
                    Modifier
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.secondaryVariant
                )
                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(25.dp, 10.dp, 25.dp, 50.dp),
                    shape = RoundedCornerShape(10.dp),
                    elevation = 4.dp,
                ) {
                    Text(
                        text = pickedWords,
                        Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .padding(12.dp),
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.secondary
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
                Text(text = stringResource(id = R.string.exit))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PickQuantityPreview() {
    KotobaCustomTheme(colorScheme = ColorScheme.pink.colors) {
        PickQuantityView(
            pickedQuantitySlider = 10f,
            pickedQuantityReal = 10,
            wordsQuantity = 20,
            onValueChange = { },
            onGoingToTest = { },
            onGoingToMain = { },
            isRememberPresent = true,
            onGoingToRemember = { },
            pickedWords = "a, b, c, d, e, f, j"
        )
    }
}