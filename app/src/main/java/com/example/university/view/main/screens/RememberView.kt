package com.example.university.view.main.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.university.R
import com.example.university.view.main.MainScreens
import com.example.university.viewModel.RememberViewModel
import com.example.university.theme.UXConstants
import org.koin.androidx.compose.koinViewModel

private const val TAG = "RememberView"

@Composable
fun RememberInit(
    navController: NavHostController,
    vm: RememberViewModel = koinViewModel(),
    listId: Int,
) {
    vm.rememberStart(listId)
    RememberScreen(navController = navController, vm = vm)
}

@Composable
fun RememberScreen(
    navController: NavHostController,
    vm: RememberViewModel,
) {

    val uiState by vm.uiState.collectAsState()
    if (uiState.isExitAlertDialogShowing) ShowExitConfirmAlert(
        onConfirm = {
            vm.hideExitAlertDialog()
            Log.i(TAG, "Перенаправление на главный экран")
            navController.navigate(MainScreens.Main.route)
        }, onReject = vm::hideExitAlertDialog
    )
    else if (uiState.isFinishAlertDialogShowing) ShowRememberFinishAlert(
        onConfirm = {
            vm.hideFinishAlertDialog()
            Log.i(TAG, "Перенаправление на главный экран")
            navController.navigate(MainScreens.Main.route)
        },
    )
    if (uiState.currentStage == 1) RememberViewSkeleton(
        word = uiState.currentWord.word,
        transcr = if (uiState.isTranscrShowing) uiState.currentWord.transcription else "",
        transl = "",
        onGoingToNext = vm::toSecondStage,
        isShowTranscriptionPresent = true,
        onShowTranscription = vm::showKana,
        onGoingToExit = vm::onExit,
        buttonText = "Продолжить"
    )
    else if (uiState.currentStage == 2) RememberViewSkeleton(
        word = uiState.currentWord.word,
        transcr = uiState.currentWord.transcription,
        transl = uiState.currentWord.translationsToString(),
        onGoingToNext = if(uiState.isItLastWord) vm::testFinish else vm::toFirstStage,
        isShowTranscriptionPresent = false,
        onShowTranscription = { },
        onGoingToExit = vm::onExit,
        buttonText = if(uiState.isItLastWord) "Закончить" else "Следующее слово"
    )
}

@Composable
fun RememberViewSkeleton(
    word: String,
    transcr: String,
    transl: String,
    buttonText: String,
    isShowTranscriptionPresent: Boolean,
    onShowTranscription: () -> Unit,
    onGoingToNext: () -> Unit,
    onGoingToExit: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(UXConstants.HORIZONTAL_PADDING, UXConstants.VERTICAL_PADDING),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        WordBanner(word = word, transcr = transcr, transl = transl)
        if (isShowTranscriptionPresent) OptionsButtons(
            onExitButtonAction = onGoingToExit,
            secondButtonLabel = "Показать транскрипцию",
            secondButtonAction = onShowTranscription
        )
        else OptionsButtons(
            onExitButtonAction = onGoingToExit,
        )
        BottomGrid {
            item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                ImgTile(
                    imgId = R.drawable.big_next_right, descr = buttonText, action = onGoingToNext
                )
            }
        }
    }
}

@Composable
fun ShowRememberFinishAlert(
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onConfirm,
        text = { Text(text = "Вы повторили все слова, тест завершен.") },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text("Выйти")
            }
        },
    )
}
