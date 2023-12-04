package com.example.university.View.Main.Screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells.*
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.university.R
import com.example.university.theme.ColorScheme
import com.example.university.theme.KotobaCustomTheme
import com.example.university.View.Main.MainScreens
import com.example.university.ViewModel.TestViewModel
import com.example.university.theme.UXConstants
import org.koin.androidx.compose.koinViewModel

private const val TAG = "TestView"

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun TestInit(
    navController: NavHostController,
    listId: Int,
    vm: TestViewModel = koinViewModel(),
) {
    vm.testStart(listId)

    TestScreen(navController = navController, vm = vm)
}

@Composable
fun TestScreen(
    navController: NavHostController,
    vm: TestViewModel,
) {
    val uiState by vm.uiState.collectAsState()
    if (uiState.isExitAlertDialogShowing)
        ShowExitConfirm(
            onConfirm = {
                vm.hideExitAlertDialog()
                Log.i("LoginView", "Перенаправление на главный экран")
                navController.navigate(MainScreens.Main.route)
            },
            onReject = vm::hideExitAlertDialog
        )
    if (uiState.isFinishAlertDialogShowing)
        ShowFinishConfirm(
            toRemember = { /*TODO*/ },
            onReject = {
                vm.hideExitAlertDialog()
                Log.i("LoginView", "Перенаправление на главный экран")
                navController.navigate(MainScreens.Main.route)
            },
            result = vm.getResult(),
            isRememberPresent = vm.msp.isRememberPresent
        )
    if (uiState.currentStage == 1)
        TestFirstStageView(
            word = uiState.wordLabel,
            transcr = uiState.transcrLabel,
            onNext = { vm.toSecondStage() },
            onShowTranscription = { vm.showKana() },
            onExit = vm::onExit,
        )
    else if (uiState.currentStage == 2)
        TestSecondStageView(
            word = uiState.wordLabel,
            transcr = uiState.transcrLabel,
            transl = uiState.translLabel,
            onGood = { vm.goodResultProcessing() },
            onBad = { vm.badResultProcessing() },
            onExit = vm::onExit,
        )
}

// На этом экране есть две фазы, меняющиеся циклично.
// Логика в них не очень сложная, так что я решил просто менять из в зависимости от currentStage в uiState
// Колхоз? Тема?
@Composable()
fun TestFirstStageView(
    word: String,
    transcr: String,
    onNext: () -> Unit,
    onShowTranscription: () -> Unit,
    onExit: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(UXConstants.HORIZONTAL_PADDING, UXConstants.VERTICAL_PADDING),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {

        WordBanner(word = word, transcr = transcr)
        OptionsButtons(
            onExitButtonAction = onExit,
            secondButtonLabel = "Показать транскрипцию",
            secondButtonAction = onShowTranscription
        )
        BottomGrid {
            item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                ImgTile(imgId = R.drawable.big_next_right, descr = "Открыть", action = onNext)
            }
        }
    }
}

@Composable()
fun TestSecondStageView(
    word: String,
    transcr: String,
    transl: String,
    onGood: () -> Unit,
    onBad: () -> Unit,
    onExit: () -> Unit,
) {

    Column(
        Modifier
            .fillMaxSize()
            .padding(UXConstants.HORIZONTAL_PADDING, UXConstants.VERTICAL_PADDING),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {

        WordBanner(word = word, transcr = transcr, transl = transl)
        OptionsButtons(
            onExitButtonAction = onExit,
        )
        BottomGrid {
            item(span = { GridItemSpan(1) }) {
                ImgTile(imgId = R.drawable.bad, descr = "Не помню", action = onBad)
            }
            item(span = { GridItemSpan(1) }) {
                ImgTile(imgId = R.drawable.good, descr = "Помню!", action = onGood)
            }
        }
    }
}

@Composable
fun WordBanner(word: String = "", transcr: String = "", transl: String = "") {
    Card(
        Modifier
            .fillMaxWidth()
            .height(150.dp),
        elevation = UXConstants.ELEVATION,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Слово
            Text(
                text = word,
                style = MaterialTheme.typography.h4,
                textAlign = TextAlign.Center,
            )
            if (transcr.isNotBlank())
            // Транскрипция
                Text(
                    text = transcr,
                    style = MaterialTheme.typography.subtitle1,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.secondaryVariant.copy(alpha = 0.8f)
                )
            if (transl.isNotBlank())
            // Перевод
                Text(
                    text = transl,
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                )
        }
    }
}

// В этой функции каждая кнопка задаётся двумя параметрами: текст и действие
// Параметров ограниченное количество и я понимаю, что это не лучшее решение,
// но я не собираюсь создавать в ней больше 2-3 кнопок, а потому решил не заморачиваться
@Composable
fun OptionsButtons(
    onExitButtonAction: () -> Unit,
    secondButtonLabel: String = "",
    secondButtonAction: () -> Unit = { },
) {
    Column(Modifier.fillMaxWidth()) {
        Button(
            onClick = onExitButtonAction,
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp, vertical = UXConstants.VERTICAL_PADDING)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.onPrimary,
                contentColor = MaterialTheme.colors.primary
            )
        ) {
            Text(text = "Выход")
        }
        if (secondButtonLabel.isNotBlank())
            Button(
                onClick = secondButtonAction,
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp)
                    .height(50.dp),
            ) {
                Text(text = secondButtonLabel)
            }
    }
}

@Composable
fun ShowExitConfirm(onConfirm: () -> Unit, onReject: () -> Unit) {
    AlertDialog(
        onDismissRequest = onReject,
        text = { Text("Вы действительно хотите закончить тестирование (его можно будеть продолжить позже)") },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text("Выйти")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onReject
            ) {
                Text("Остаться")
            }
        }
    )
}

@Composable
fun ShowFinishConfirm(
    toRemember: () -> Unit,
    onReject: () -> Unit,
    result: Double,
    isRememberPresent: Boolean
) {
    var text = "Вы прошли тест с результатом $result%."
    if (isRememberPresent)
        text += "\nХотите ли вы ещё раз пройтись по этим словам?"
    AlertDialog(
        onDismissRequest = onReject,
        text = { Text(text = text) },
        confirmButton = {
            TextButton(
                onClick = onReject
            ) {
                Text("Выйти")
            }
        },
        dismissButton = {
            if (isRememberPresent)
                TextButton(
                    onClick = toRemember
                ) {
                    Text("Остаться")
                }
        }
    )
}

@Composable
fun BottomGrid(content: LazyGridScope.() -> Unit) {
    LazyVerticalGrid(
        columns = Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        contentPadding = PaddingValues(0.dp, 15.dp, 0.dp, 15.dp)
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ImgTile(imgId: Int, descr: String, action: () -> Unit) {
    Card(
        shape = MaterialTheme.shapes.small,
        elevation = UXConstants.ELEVATION - 1.dp,
        onClick = { action() }) {

        Box(Modifier.padding(20.dp)) {

            Column(
                Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = ImageVector.vectorResource(imgId),
                    contentDescription = descr,
                    tint = MaterialTheme.colors.primary,
                )

                Text(
                    text = descr,
                    Modifier
                        .padding(top = 5.dp, bottom = 0.dp),
                    fontSize = 20.sp,
                    color = MaterialTheme.colors.secondaryVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FirstStagePreview() {
    KotobaCustomTheme(colorScheme = ColorScheme.pink.colors) {
        TestFirstStageView(
            word = "sample",
            transcr = "sample",
            onNext = { },
            onExit = { },
            onShowTranscription = { })
    }
}

@Preview(showBackground = true)
@Composable
fun SecondStagePreview() {
    KotobaCustomTheme(colorScheme = ColorScheme.pink.colors) {
        TestSecondStageView(
            word = "sample",
            transcr = "sample",
            transl = "Пример, пример",
            onExit = { },
            onBad = { },
            onGood = { },
        )
    }
}