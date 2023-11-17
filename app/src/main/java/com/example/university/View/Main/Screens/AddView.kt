package com.example.university.View.Main.Screens

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.university.View.Main.MainActivity
import com.example.university.View.Main.MainScreens
import com.example.university.ViewModel.AddViewModel
import com.example.university.ViewModel.MainViewModel
import com.example.university.theme.ColorScheme
import com.example.university.theme.KotobaCustomTheme

private val TAG = "AddView"

@Composable
fun addScreen(context: MainActivity, navController: NavHostController, vm: AddViewModel) {
    val uiState by vm.uiState.collectAsState()
    KotobaCustomTheme(colorScheme = uiState.colorScheme) {
        context.window.statusBarColor = MaterialTheme.colors.primary.toArgb()
        addView(
            word = uiState.wordValue,
            onWordChanged = { vm.editWordValue(it) },
            transcription = uiState.transcrValue,
            onTranscrChanged = { vm.editTranscrValue(it) },
            translations = uiState.translValues,
            onTranslChanged = { value, id -> vm.editTranslationValue(value, id) },
            onAddTranstation = { vm.addTranslation() },
            lvl = uiState.lvlValue,
            onLvlChanged = { vm.editLvlValue(it) },
            onConfirm = { vm.addWord() },
            isWordFieldWrong = uiState.isWordFieldWrong,
            isTranslFieldWrong = uiState.isTranslFieldWrong,
            isLvlFieldWrong = uiState.isLvlFieldWrong,
            errorMessage = uiState.errorMessage,
            onGoingToMain = {
                Log.i(TAG, "Перенаправление на главный экран")
                navController.navigate(MainScreens.AddNew.route)
            }
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun addView(
    word: String,
    onWordChanged: (String) -> Unit,
    transcription: String,
    onTranscrChanged: (String) -> Unit,
    translations: List<String>,
    onTranslChanged: (String, Int) -> Unit,
    onAddTranstation: () -> Unit,
    lvl: String,
    onLvlChanged: (String) -> Unit,
    onConfirm: () -> Unit,
    isWordFieldWrong: Boolean,
    isTranslFieldWrong: Boolean,
    isLvlFieldWrong: Boolean,
    errorMessage: String,
    onGoingToMain: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
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
            text = "Добавьте новое слово",
            modifier = Modifier
                .fillMaxWidth()
                .padding(),
            textAlign = TextAlign.Center,
            //style = TextStyle(fontWeight = FontWeight.Bold),
            fontSize = 20.sp,
        )

        OutlinedTextField(
            value = word,
            onValueChange = { onWordChanged(it) },
            label = {
                if (isWordFieldWrong)
                    Text(text = errorMessage)
                else
                    Text("Слово")
            },
            isError = isWordFieldWrong,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
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
                .padding(top = 20.dp),
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
                onValueChange = { onTranslChanged(it, index.toInt()) },
                label = {
                    if (isTranslFieldWrong && index == 0)
                        Text(text = errorMessage)
                    else
                        Text("Перевод")
                },
                isError = isTranslFieldWrong,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
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
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 7.dp,)
                .clickable { onAddTranstation() },
            text = "Добавить перевод",
            fontSize = 15.sp,
            color = MaterialTheme.colors.secondary,
            textAlign = TextAlign.Right,
        )


        OutlinedTextField(
            value = lvl,
            onValueChange = { onLvlChanged(it) },
            label = {
                if (isLvlFieldWrong)
                    Text(text = errorMessage)
                else
                    Text("Период показа")
            },
            isError = isLvlFieldWrong,
            singleLine = true,
            placeholder = {
                Text("0")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(onDone = {
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


        Button(
            onClick = {
                onConfirm()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 35.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary
            )
        ) {
            Text(text = "Добавить")
        }

        Button(
            onClick = {
                onGoingToMain()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.onPrimary,
                contentColor = MaterialTheme.colors.primary
            )
        ) {
            Text(text = "Выйти")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddViewPreview() {
    KotobaCustomTheme(colorScheme = ColorScheme.pink.colors) {
        addView(
            word = "Example",
            onWordChanged = { },
            transcription = "Example",
            onTranscrChanged = { },
            translations = listOf("Example"),
            onTranslChanged = { str, int -> },
            onAddTranstation = { /*TODO*/ },
            lvl = "13",
            onLvlChanged = {},
            onConfirm = { /*TODO*/ },
            isWordFieldWrong = false,
            isTranslFieldWrong = false,
            isLvlFieldWrong = false,
            errorMessage = "",
        ) {

        }
    }
}