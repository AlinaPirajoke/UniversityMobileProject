package com.example.university.view.main.screens

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.university.R
import com.example.university.theme.ColorScheme
import com.example.university.theme.KotobaCustomTheme
import com.example.university.theme.UXConstants
import com.example.university.usefullStuff.DownloadAnimation
import com.example.university.usefullStuff.Word
import com.example.university.view.main.MainScreens
import com.example.university.viewModel.PickWordViewModel
import org.koin.androidx.compose.koinViewModel

private const val TAG = "PickWordView"

@Composable
fun PickWordScreen(
    navController: NavHostController,
    vm: PickWordViewModel = koinViewModel(),
) {
    val uiState by vm.uiState.collectAsState()

    if (uiState.isAlertShowing) {
        WordDeleteConfirm(onConfirm = vm::wordIsAlreadyLearned, onReject = { vm.deleteWordRequest(false) })
    }
    if (uiState.isGoingToMain) {
        vm.sendToMain(false)
        Log.i(TAG, "Перенаправление на главный экран")
        navController.navigate(MainScreens.Main.route)
    }
    PickWordView(
        words = uiState.words,
        pickedWords = uiState.pickedWords,
        onPick = vm::pickWord,
        onUnpick = vm::unpickWord,
        onConfirm = vm::confirmWordAdding,
        topText = uiState.topText,
        onExit = {
            Log.i(TAG, "Перенаправление на главный экран")
            navController.navigate(MainScreens.Main.route)
        },
        onAlreadyLearned = { i :Int -> vm.deleteWordRequest(true, i) }
    )
}

@Composable
fun PickWordView(
    words: List<Word>,
    pickedWords: List<Int>,
    onPick: (Int) -> Unit,
    onUnpick: (Int) -> Unit,
    onConfirm: () -> Unit,
    topText: String,
    onExit: () -> Unit,
    onAlreadyLearned: (Int) -> Unit
) {
    Scaffold(
        Modifier.padding(0.dp),
        backgroundColor = MaterialTheme.colors.background,
        floatingActionButton = {
            ExitConfirmFloatingActionButton(onExit = onExit, onConfirm = onConfirm)
        }
    )
    {
        Log.i(TAG, it.toString()) // Я в ахуях, надо было где-то it использовать
        Column(
            Modifier
                .padding(
                    top = UXConstants.VERTICAL_PADDING,
                    start = UXConstants.HORIZONTAL_PADDING,
                    end = UXConstants.HORIZONTAL_PADDING
                )
                .fillMaxSize()

        ) {
            Text(
                text = topText,
                Modifier.fillMaxWidth(),
                textAlign = TextAlign.Left,
                style = MaterialTheme.typography.h5
            )
            LazyColumn(
                Modifier
                    .padding(top = UXConstants.VERTICAL_PADDING)
                    .fillMaxWidth(),
            ) {
//                item() {
//                    ConfirmTile(
//                        onConfirm = onConfirm,
//                        text = if (pickedWords.size > 0) "Подтвердить выбор ${pickedWords.size} ${if (pickedWords.size == 1) "слова" else "слов"}" else "Выберите хотя бы одно слово"
//                    )
//                }
                words.forEachIndexed() { i, word ->
                    item {
                        if (i in pickedWords)
                            ListTile(
                                word = word,
                                onClick = { onUnpick(i) },
                                isPicked = true,
                                onAlreadyLearned = { onAlreadyLearned(i) },
                            )
                        else
                            ListTile(word = word, onClick = { onPick(i) }, isPicked = false)
                    }
                }
            }
        }
        if (words.size == 0)
            DownloadAnimation()
    }
}


@Composable
fun ConfirmTile(onConfirm: () -> Unit, text: String) {
    Card(
        Modifier
            .padding(vertical = UXConstants.VERTICAL_PADDING / 2)
            .height(80.dp)
            .fillMaxWidth(),
        elevation = UXConstants.ELEVATION,
        shape = MaterialTheme.shapes.small
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, bottom = 3.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextButton(onClick = onConfirm, Modifier.fillMaxSize()) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.body1.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.W500
                    )
                )
            }
        }
    }
}

@Composable
fun ListTile(
    word: Word,
    onClick: () -> Unit,
    isPicked: Boolean,
    onAlreadyLearned: (Int) -> Unit = { }
) {
    var modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = UXConstants.VERTICAL_PADDING / 2)
        .height(80.dp)
        .clickable { onClick() }
    var elevation = 2.dp

    if (isPicked) {
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = UXConstants.VERTICAL_PADDING / 2)
            .height(80.dp)
            .clickable { onClick() }
            .border(
                width = 2.dp,
                color = MaterialTheme.colors.primary,
                shape = MaterialTheme.shapes.small
            )
        elevation = 0.dp
    }

    Card(
        modifier = modifier,
//        elevation = if (isPicked) 1.dp else 3.dp,
        elevation = elevation,
        shape = MaterialTheme.shapes.small,
    ) {
        Row(
            Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            WordDescriptionColumn(word = word)
            if (isPicked) {
                Row(
                    Modifier
                        .padding(0.dp)
                        .fillMaxWidth()
                        .weight(1f),
                    Arrangement.End
                ) {
                    TextButton(onClick = { onAlreadyLearned(word.id) }) {
                        Text(
                            text = stringResource(id = R.string.PW_already_learned),
                            Modifier.padding(end = UXConstants.HORIZONTAL_PADDING)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExitConfirmFloatingActionButton(
    onExit: () -> Unit,
    onConfirm: () -> Unit,
) {
    Column(horizontalAlignment = Alignment.End) {
        ExitConfirmFloatingActionButtonPart(
            text = stringResource(id = R.string.add),
            img = ImageVector.vectorResource(R.drawable.good),
            onClick = onConfirm
        )
        ExitConfirmFloatingActionButtonPart(
            text = stringResource(id = R.string.exit),
            img = ImageVector.vectorResource(R.drawable.bad),
            onClick = onExit
        )
    }
}

@Composable
fun ExitConfirmFloatingActionButtonPart(text: String, img: ImageVector, onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        Modifier
            .padding(top = 10.dp)
            .width(130.dp)
            .height(50.dp),
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            Modifier
                .fillMaxSize()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                modifier = Modifier.padding(start = 8.dp).size(20.dp),
                imageVector = img,
                contentDescription = text,
                tint = MaterialTheme.colors.onPrimary
            )
            Text(
                text = text,
                Modifier.fillMaxWidth(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body2
            )
        }
    }
}

@Composable
fun WordAlreadyLearnedConfirm(onConfirm: () -> Unit, onReject: () -> Unit) {
    AlertDialog(
        onDismissRequest = onReject,
        text = { Text("Вы действительно хорошоо знаете это слово и хотите исключить слово из библиотеки?") },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text("Подтвердить")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onReject
            ) {
                Text("Отменить")
            }
        }
    )
}

@Preview(showBackground = false)
@Composable
fun ListRowPreview() {
    ListTile(
        Word(
            id = 1,
            word = "sample",
            transcription = "sample",
            translations = listOf("sample"),
            lvl = 1,
        ),
        onClick = {},
        isPicked = true
    )
}

@Preview(showBackground = true)
@Composable
fun PickWordPreview() {
    val word = Word(
        id = 1,
        word = "sample",
        transcription = "sample",
        translations = listOf("sample"),
        lvl = 1,
    )
    val words = listOf(word, word, word)
    KotobaCustomTheme(colorScheme = ColorScheme.mint.colors) {
        PickWordView(
            words = words,
            pickedWords = listOf(1),
            onPick = { },
            onConfirm = { },
            onUnpick = { },
            topText = "pdfjosfdhsetws",
            onExit = { },
            onAlreadyLearned = { }
        )
    }
}