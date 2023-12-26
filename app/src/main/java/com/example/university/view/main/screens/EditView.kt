package com.example.university.view.main.screens

import android.util.Log
import androidx.compose.foundation.clickable
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.university.R
import com.example.university.theme.ColorScheme
import com.example.university.theme.KotobaCustomTheme
import com.example.university.theme.UXConstants
import com.example.university.view.main.MainScreens
import com.example.university.viewModel.EditViewModel
import org.koin.androidx.compose.koinViewModel

private const val TAG = "EditView"

@Composable
fun EditInit(
    navController: NavHostController,
    vm: EditViewModel = koinViewModel(),
    wordId: Int,
) {
    vm.modifiedWordId = wordId
    EditScreen(navController = navController, vm = vm)
}

@Composable
fun EditScreen(
    navController: NavHostController,
    vm: EditViewModel
) {
    val uiState by vm.uiState.collectAsState()
    if (uiState.isGoingToExit) {
        vm.sendToExit(false)
        Log.i(TAG, "Перенаправление на экран словаря пользователя")
        navController.navigate(MainScreens.UserWords.route)
    }

    EditView(
        word = uiState.wordValue,
        onWordChanged = vm::editWordValue,
        transcription = uiState.transcrValue,
        onTranscrChanged = vm::editTranscrValue,
        translations = uiState.translValues,
        onTranslChanged = vm::editTranslationValue,
        onAddTranstation = vm::addTranslation,
        onConfirm = vm::editWord,
        isWordFieldWrong = uiState.isWordFieldWrong,
        isTranslFieldWrong = uiState.isTranslFieldWrong,
        errorMessage = uiState.errorMessage,
        onGoingToExit = {
            Log.i(TAG, "Перенаправление на экран словаря пользователя")
            navController.navigate(MainScreens.UserWords.route)
        },
    )
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun EditView(
    word: String,
    onWordChanged: (String) -> Unit,
    transcription: String,
    onTranscrChanged: (String) -> Unit,
    translations: List<String>,
    onTranslChanged: (String, Int) -> Unit,
    onAddTranstation: () -> Unit,
    onConfirm: () -> Unit,
    isWordFieldWrong: Boolean,
    isTranslFieldWrong: Boolean,
    errorMessage: String,
    onGoingToExit: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    Column(
        Modifier
            .fillMaxHeight()
            .padding(40.dp, UXConstants.VERTICAL_PADDING)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Отредактируйте слово",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = UXConstants.VERTICAL_PADDING),
            textAlign = TextAlign.Left,
            style = MaterialTheme.typography.h5
        )

        OutlinedTextField(
            value = word,
            onValueChange = { onWordChanged(it) },
            label = {
                if (isWordFieldWrong) Text(text = errorMessage)
                else Text("Иностранное слово")
            },
            isError = isWordFieldWrong,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
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

        OutlinedTextField(
            value = transcription,
            onValueChange = { onTranscrChanged(it) },
            label = { Text("Транскрипция (необязательно)") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = UXConstants.VERTICAL_PADDING + 10.dp),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
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

        translations.forEachIndexed { index, translation ->
            Log.d(TAG, "Значение перевода $index - $translation")
            OutlinedTextField(
                value = translation,
                onValueChange = { onTranslChanged(it, index) },
                label = {
                    if (isTranslFieldWrong && index == 0) Text(text = errorMessage)
                    else Text("Перевод")
                },
                isError = isTranslFieldWrong,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = UXConstants.VERTICAL_PADDING + 10.dp),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = {
                    keyboardController?.hide()
                    onConfirm()
                }),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colors.primary,
                    unfocusedBorderColor = MaterialTheme.colors.primary,
                    cursorColor = MaterialTheme.colors.primary,
                    focusedLabelColor = MaterialTheme.colors.secondary,
                )
            )
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp)
                .clickable { onAddTranstation() },
            text = "Добавить перевод",
            color = MaterialTheme.colors.secondary,
            textAlign = TextAlign.Right,
            style = MaterialTheme.typography.subtitle1
        )

        Button(
            onClick = onConfirm,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary
            )
        ) {
            Text(text = stringResource(id = R.string.confirm))
        }

        Button(
            onClick = {
                onGoingToExit()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.onPrimary,
                contentColor = MaterialTheme.colors.primary
            )
        ) {
            Text(text = stringResource(id = R.string.exit))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditViewPreview() {
    KotobaCustomTheme(colorScheme = ColorScheme.pink.colors) {
        EditView(
            word = "Example",
            onWordChanged = { },
            transcription = "Example",
            onTranscrChanged = { },
            translations = listOf("Example"),
            onTranslChanged = { str, int -> },
            onAddTranstation = { },
            onConfirm = { },
            isWordFieldWrong = false,
            isTranslFieldWrong = false,
            errorMessage = "",
            onGoingToExit = { },
        )
    }
}
