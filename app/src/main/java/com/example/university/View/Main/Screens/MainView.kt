package com.example.university.View.Main.Screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.example.university.View.Main.MainActivity
import com.example.university.ViewModel.MainViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.university.R
import com.example.university.View.Main.MainScreens
import com.example.university.theme.ColorScheme
import com.example.university.theme.KotobaCustomTheme

private val TAG = "MainView"

@Composable
fun mainScreen(context: MainActivity, navController: NavHostController, vm: MainViewModel) {
    val uiState by vm.uiState.collectAsState()
//  if (uiState.isGoingToTest) context.toTest()
    KotobaCustomTheme(colorScheme = uiState.colorScheme) {
        context.window.statusBarColor = MaterialTheme.colors.primary.toArgb()
        mainView(
            statLearned = uiState.statLearned,
            statLearning = uiState.statLearning,
            statAverage = uiState.statAverage,
            todayTest = uiState.todayTest,
            todayLearn = uiState.todayLearn,
            onGoingToPickCount = { },
            onGoingToPickWords = { },
            toAddNew = {
                Log.i(TAG, "Перенаправление на экран добавления слов")
                navController.navigate(MainScreens.AddNew.route)
            },
            toSettings = {
                Log.i(TAG, "Перенаправление на экран настроек")
                navController.navigate(MainScreens.Settings.route)
            },
            toLogin = context::toLogin,
        )
    }

}

val BORDER_PADDING = 12.dp

@Composable
fun mainView(
    statLearned: Int,
    statLearning: Int,
    statAverage: Int,
    todayTest: Int,
    todayLearn: Int,
    onGoingToPickCount: () -> Unit,
    onGoingToPickWords: () -> Unit,
    toAddNew: () -> Unit,
    toLogin: () -> Unit,
    toSettings: () -> Unit,
) {
    val scrollState = rememberScrollState()
    Column(
        Modifier
            .fillMaxSize()
            .padding(0.dp, 0.dp, 0.dp, 0.dp)
            .verticalScroll(scrollState),
    ) {
        statisticBlock(
            learned = statLearned,
            learning = statLearning,
            average = statAverage,
        )
        todayBlock(
            todayTest = todayTest,
            todayLearn = todayLearn,
            onGoingToPickCount = onGoingToPickCount,
            onGoingToPickWords = onGoingToPickWords,
        )
        otherBlock(
            onGoingToSettings = { toSettings() },
            onGoingToAddNew = { toAddNew() },
            onGoingToLogin = { toLogin() },
        )
    }
}

@Composable
fun statisticBlock(
    learned: Int = 0,
    learning: Int = 0,
    average: Int = 0,
) {
    Column {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            text = "Статистика изучения",
            fontSize = 15.sp,
            color = MaterialTheme.colors.secondaryVariant,
            textAlign = TextAlign.Center
        )
        Card(
            Modifier
                .fillMaxWidth()
                .padding(BORDER_PADDING, 20.dp),
            shape = RoundedCornerShape(10.dp),
            elevation = 4.dp,
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                Arrangement.SpaceBetween,
            ) {
                statisticLine(text = "Всего слов изучено", value = learned)
                statisticLine(text = "Слов изучается", value = learning)
                statisticLine(text = "Среднее изучаемое в день", value = average)
            }
        }
    }
}

@Composable
fun todayBlock(
    todayTest: Int = 0,
    todayLearn: Int = 0,
    onGoingToPickCount: () -> Unit,
    onGoingToPickWords: () -> Unit,
) {

    Column(
        Modifier
            .fillMaxWidth()
            .padding(BORDER_PADDING, 50.dp), verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            text = "План на сегодня",
            fontSize = 15.sp,
            color = MaterialTheme.colors.secondaryVariant,
            textAlign = TextAlign.Center
        )
        todayAction(text = "$todayTest осталось повторить", onGoingToPickWords)
        todayAction(text = "$todayLearn слов осталось изучить", onGoingToPickCount)
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            text = "Посмотреть другие даты",
            fontSize = 15.sp,
            color = MaterialTheme.colors.primary,
            textAlign = TextAlign.Right
        )
    }
}

@Composable
fun otherBlock(
    onGoingToSettings: () -> (Unit),
    onGoingToAddNew: () -> (Unit),
    onGoingToLogin: () -> (Unit),
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp),
        text = "Другие действия",
        fontSize = 15.sp,
        color = MaterialTheme.colors.secondaryVariant,
        textAlign = TextAlign.Center
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        shape = RoundedCornerShape(30.dp, 30.dp, 0.dp, 0.dp),
        elevation = 4.dp,
        backgroundColor = MaterialTheme.colors.primaryVariant,
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(BORDER_PADDING, 28.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp),
        ) {
            /*Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(15.dp),) {}*/
            createImgCard(imgId = R.drawable.setings, descr = "Настройки", onGoingToSettings)
            createImgCard(imgId = R.drawable.add, descr = "Добавить слово", onGoingToAddNew)
            createImgCard(imgId = R.drawable.logout, descr = "Разлогиниться", onGoingToLogin)
        }
    }
}

@Composable
fun statisticLine(text: String, value: Int) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, bottom = 5.dp),
        Arrangement.SpaceBetween,
    ) {
        Text(modifier = Modifier.alpha(0.8f), text = text, fontSize = 20.sp)
        Text(modifier = Modifier.alpha(0.9f), text = value.toString(), fontSize = 20.sp)
    }
}

@Composable
fun todayAction(text: String, action: () -> Unit) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(top = 15.dp),
        shape = RoundedCornerShape(7.dp),
        elevation = 4.dp,
    ) {
        Text(
            modifier = Modifier.padding(15.dp, 20.dp),
            text = text,
            color = MaterialTheme.colors.primary,
            fontSize = 25.sp
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun createImgCard(imgId: Int, descr: String, action: () -> Unit) {
    Card(shape = RoundedCornerShape(10.dp), elevation = 4.dp, onClick = { action.invoke() }) {

        Box(Modifier.padding(20.dp)) {

            Column(
                Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween
            ) {

                /*Image(
                    painter = painterResource(id = imgId),
                    contentDescription = descr,
                    modifier = Modifier.size(40.dp),
                )*/
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = ImageVector.vectorResource(imgId),
                    contentDescription = descr,
                    tint = MaterialTheme.colors.primary,
                )

                Text(
                    text = descr,
                    Modifier
                        .alpha(0.8f)
                        .padding(top = 5.dp, bottom = 0.dp),
                    fontSize = 20.sp,
                    color = MaterialTheme.colors.secondaryVariant
                )
            }
        }
    }
}