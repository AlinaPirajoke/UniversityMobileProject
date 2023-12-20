package com.example.university.View.Main.Screens

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.university.R
import com.example.university.UsefullStuff.Word
import com.example.university.View.Main.MainScreens
import com.example.university.ViewModel.UserWordsViewModel
import com.example.university.theme.ColorScheme
import com.example.university.theme.KotobaCustomTheme
import com.example.university.theme.UXConstants
import org.koin.androidx.compose.koinViewModel

private const val TAG = "UserWordsView"

@Composable
fun UserWordsScreen(
    navController: NavHostController,
    vm: UserWordsViewModel = koinViewModel(),
) {
    val uiState by vm.uiState.collectAsState()
    if (uiState.isAlertShowing) {
        WordDeleteConfirm(onConfirm = vm::DeleteWordConfirm, onReject = vm::DeleteWordReject)
    }
    UserWordsView(
        words = uiState.words,
        pickedWordNo = uiState.pickedWordNo,
        onPick = vm::pickElement,
        onEdit = { /*TODO*/ },
        onDelete = vm::deleteWord,
        onExit = {
            Log.i(TAG, "Перенаправление на главный экран")
            navController.navigate(MainScreens.Main.route)
        },
    )
}

@Composable
fun UserWordsView(
    words: ArrayList<Word>,
    pickedWordNo: Int,
    onPick: (Int) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onExit: () -> Unit
) {
    Scaffold(
        Modifier.padding(0.dp),
        backgroundColor = MaterialTheme.colors.background,
        floatingActionButton = {
            ExitConfirmFloatingActionButtonPart(
                onClick = onExit,
                text = stringResource(id = R.string.exit),
                img = ImageVector.vectorResource(R.drawable.bad),
            )
        }
    )
    { innerPadding ->
        println(innerPadding.toString())

        Column(
            Modifier
                .fillMaxSize()
                .padding(
                    top = UXConstants.VERTICAL_PADDING,
                    start = UXConstants.HORIZONTAL_PADDING,
                    end = UXConstants.HORIZONTAL_PADDING
                )
        ) {
            Text(
                text = "Все изучаемые слова (${words.size}):",
                Modifier.fillMaxWidth(),
                textAlign = TextAlign.Left,
                style = MaterialTheme.typography.h5
            )
            LazyColumn(
                Modifier
                    .fillMaxWidth()
                    .padding(top = UXConstants.VERTICAL_PADDING),
            ) {
                words.forEachIndexed() { i, word ->
                    item {
                        if (i == pickedWordNo)
                            ActiveListTile(word = word, onDelete = onDelete, onEdit = onEdit)
                        else
                            InactiveListTile(word = word, onClick = { onPick(i) })
                    }
                }
            }
        }
    }
}

@Composable
fun InactiveListTile(
    word: Word,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = UXConstants.VERTICAL_PADDING / 2)
            .height(80.dp)
            .clickable { onClick() },
        elevation = UXConstants.ELEVATION.div(2),
        shape = MaterialTheme.shapes.small,
    ) {
        WordDescriptionColumn(word = word)
    }
}

@Composable
fun ActiveListTile(
    word: Word,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = UXConstants.VERTICAL_PADDING / 2)
            .height(80.dp)
            .border(
                width = 2.dp,
                color = MaterialTheme.colors.primary,
                shape = MaterialTheme.shapes.small
            ),
        elevation = UXConstants.ELEVATION,
        shape = MaterialTheme.shapes.small,
    ) {
        Row(
            Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            WordDescriptionColumn(word = word)
            Row(
                Modifier
                    .padding(0.dp)
                    .fillMaxWidth()
                    .weight(1f),
                Arrangement.End
            ) {
                TextButton(onClick = onDelete) {
                    Text(
                        text = stringResource(id = R.string.delete),
                        Modifier.padding(end = UXConstants.HORIZONTAL_PADDING)
                    )
                }
//                TextButton(onClick = onEdit) {
//                    Text(text = stringResource(id = R.string.edit))
//                }
            }
        }
    }
}

@Composable
fun WordDescriptionColumn(word: Word) {
    Row {
        Column(
            Modifier
                .padding(start = 10.dp, bottom = 3.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ) {
            // Слово
            Text(
                text = word.word,
                style = MaterialTheme.typography.body2,
                textAlign = TextAlign.Start,
            )
            if (word.transcription.isNotBlank()) {
                // Транскрипция
                Text(
                    text = word.transcription,
                    style = MaterialTheme.typography.subtitle2,
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colors.secondaryVariant.copy(alpha = 0.8f)
                )
            }
            // Перевод
            Text(
                text = word.translationsToString(),
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Start,
            )
        }

//        Column(
//            Modifier
//                .padding(start = 10.dp, bottom = 3.dp)
//                .fillMaxHeight(),
//            verticalArrangement = Arrangement.SpaceEvenly,
//            horizontalAlignment = Alignment.Start
//        ) {
//            Text(text = "")
//            Text(text = stringResource(id = R.string.UW_lvl_description) + " " + word.lvl.toString())
//            Text(text = stringResource(id = R.string.UW_date_description) + " " + word.comming)
//        }
    }
}


@Composable
fun WordDeleteConfirm(onConfirm: () -> Unit, onReject: () -> Unit) {
    AlertDialog(
        onDismissRequest = onReject,
        text = { Text("Вы действительно хотите удалить слово из словаря?") },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text("Удалить")
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

/*@Composable
fun EFloatingActionButton(
    onExit: () -> Unit
){
    FloatingActionButton(
        onClick = onExit,
        Modifier
            .width(100.dp)
            .height(50.dp),
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = ImageVector.vectorResource(R.drawable.bad),
                contentDescription = "exit",
                tint = MaterialTheme.colors.onPrimary
            )
            Text(text = "Выйти", style = MaterialTheme.typography.body2,)
        }

    }
}*/

@Preview(showBackground = true)
@Composable
fun UserWordsPreview() {
    val word = Word(
        id = 1,
        word = "sample",
        transcription = "sample",
        translations = listOf("sample"),
        lvl = 1,
    )
    val words = arrayListOf(word, word, word)
    KotobaCustomTheme(colorScheme = ColorScheme.mint.colors) {
        UserWordsView(
            words = words,
            onPick = { },
            onDelete = { },
            onEdit = { },
            pickedWordNo = 1,
            onExit = { }
        )
    }
}
