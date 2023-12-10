package com.example.university.View.Main.Screens

import android.util.Log
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.university.R
import com.example.university.UsefullStuff.getTodayDate
import com.example.university.View.Main.MainScreens
import com.example.university.ViewModel.MainViewModel
import com.example.university.theme.UXConstants
import org.koin.androidx.compose.koinViewModel

private const val TAG = "MainView"

@Composable
fun MainScreen(
    onGoingToLogin: () -> Unit,
    navController: NavHostController,
    vm: MainViewModel = koinViewModel()
) {
    val uiState by vm.uiState.collectAsState()
    MainView(
        statLearned = uiState.statLearned,
        statLearning = uiState.statLearning,
        statAverage = uiState.statAverage,
        todayTest = uiState.todayTest,
        todayLearn = uiState.todayLearn,
        onGoingToPickQuantity = {
            if (uiState.todayTest > 0) {
                Log.i(TAG, "Перенаправление на экран выбора количества слов для тестирования")
                navController.navigate("${MainScreens.PickQuantity.route}/${getTodayDate()}")
            }
        },
        onGoingToPickWords = {
            Log.i(TAG, "Перенаправление на экран выбора слов")
            navController.navigate(MainScreens.PickWord.route)
        },
        toAddNew = {
            Log.i(TAG, "Перенаправление на экран добавления слов")
            navController.navigate(MainScreens.AddNew.route)
        },
        toSettings = {
            Log.i(TAG, "Перенаправление на экран настроек")
            navController.navigate(MainScreens.Settings.route)
        },
        toAnotherDates = {
            Log.i(TAG, "Перенаправление на экран будующих тестов")
            navController.navigate(MainScreens.FutureTests.route)
        },
        toLogin = onGoingToLogin,
        toUserWords = {
            Log.i(TAG, "Перенаправление на экран пользовательского словаря")
            navController.navigate(MainScreens.UserWords.route)
        }
    )
}

@Composable
fun MainView(
    statLearned: Int,
    statLearning: Int,
    statAverage: Double,
    todayTest: Int,
    todayLearn: Int,
    onGoingToPickQuantity: () -> Unit,
    onGoingToPickWords: () -> Unit,
    toAddNew: () -> Unit,
    toLogin: () -> Unit,
    toSettings: () -> Unit,
    toAnotherDates: () -> Unit,
    toUserWords: () -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        Modifier
            .fillMaxSize()
            .padding(0.dp, 0.dp, 0.dp, 0.dp)
            .verticalScroll(scrollState),
    ) {
        StatisticBlock(
            learned = statLearned,
            learning = statLearning,
            average = statAverage,
        )
        TodayBlock(
            todayTest = todayTest,
            todayLearn = todayLearn,
            onGoingToPickCount = onGoingToPickQuantity,
            onGoingToPickWords = onGoingToPickWords,
            toAnotherDates = toAnotherDates,
        )
        OtherBlock(
            onGoingToSettings = toSettings,
            onGoingToAddNew = toAddNew,
            onGoingToLogin = toLogin,
            onGoingToDictionary = toUserWords
        )
    }
}

@Composable
fun StatisticBlock(
    learned: Int = 0,
    learning: Int = 0,
    average: Double = 0.0,
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
                .padding(
                    UXConstants.HORIZONTAL_PADDING,
                    20.dp,
                    UXConstants.HORIZONTAL_PADDING,
                    UXConstants.HORIZONTAL_PADDING
                ),
            shape = RoundedCornerShape(10.dp),
            elevation = 4.dp,
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                Arrangement.SpaceBetween,
            ) {
                StatisticLine(text = "Всего слов изучено", value = learned)
                StatisticLine(text = "Слов изучается", value = learning)
                StatisticLine(text = "Среднее изучаемое в день", value = average)
            }
        }
    }
}

@Composable
fun TodayBlock(
    todayTest: Int = 0,
    todayLearn: Int = 0,
    onGoingToPickCount: () -> Unit,
    onGoingToPickWords: () -> Unit,
    toAnotherDates: () -> Unit,
) {

    Column(
        Modifier
            .fillMaxWidth()
            .padding(UXConstants.HORIZONTAL_PADDING, 50.dp),
        verticalArrangement = Arrangement.SpaceBetween
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
        TodayAction(text = "$todayTest осталось повторить", onGoingToPickCount)
        TodayAction(text = "$todayLearn слов осталось изучить", onGoingToPickWords)
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .clickable { toAnotherDates() },
            text = "Посмотреть другие даты",
            fontSize = 15.sp,
            color = MaterialTheme.colors.primary,
            textAlign = TextAlign.Right
        )
    }
}

@Composable
fun OtherBlock(
    onGoingToSettings: () -> (Unit),
    onGoingToAddNew: () -> (Unit),
    onGoingToLogin: () -> (Unit),
    onGoingToDictionary: () -> Unit
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
        shape = RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp),
        elevation = 4.dp,
        backgroundColor = MaterialTheme.colors.primaryVariant,
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(UXConstants.HORIZONTAL_PADDING, 20.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp),
        ) {
            /*Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(15.dp),) {}*/
            ImgCard(imgId = R.drawable.setings, descr = "Настройки", onGoingToSettings)
            ImgCard(imgId = R.drawable.add, descr = "Добавить слово", onGoingToAddNew)
            ImgCard(imgId = R.drawable.logout, descr = "Разлогиниться", onGoingToLogin)
            ImgCard(imgId = R.drawable.dictionary, descr = "Мой словарь", onGoingToDictionary)
        }
    }
}

@Composable
fun StatisticLine(text: String, value: Int) {
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
fun StatisticLine(text: String, value: Double) {
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
fun TodayAction(text: String, action: () -> Unit) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(top = 15.dp)
            .clickable { action() },
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
fun ImgCard(imgId: Int, descr: String, action: () -> Unit) {
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