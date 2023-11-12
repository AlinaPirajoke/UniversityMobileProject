package com.example.university.View.Main

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.university.Model.MySharedPreferences
import com.example.university.R
import com.example.university.View.Auth.AuthActivity
import com.example.university.ViewModel.MainViewModel
import com.example.university.ViewModel.MainViewModelFactory
import com.example.university.ViewModel.SettingsViewModel
import com.example.university.theme.ColorScheme
import com.example.university.theme.KotobaCustomTheme
import com.example.university.usefull_stuff.StringInt
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    val BORDER_PADDING = 12.dp
    val vm by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "Инициализация...")
        super.onCreate(savedInstanceState)

        setContent {
            val scrollState = rememberScrollState()
            val uiState by vm.uiState.collectAsState()
            if (uiState.isGoingToLogin)
                this.toLogin()
            
            KotobaCustomTheme(colorScheme = uiState.colorScheme) {
                window.statusBarColor = MaterialTheme.colors.primary.toArgb()
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(0.dp, 0.dp, 0.dp, 0.dp)
                        .verticalScroll(scrollState),
                ) {
                    statisticBlock(
                        learned = uiState.statLearned,
                        learning = uiState.statLearning,
                        average = uiState.statAverage,
                    )
                    todayBlock(
                        todayTest = uiState.todayTest,
                        todayLearn = uiState.todayLearn,
                        onGoingToTest = { },
                        onGoingToLearn = { },
                    )
                    otherBlock(
                        onGoingToSettings = { toSettings() },
                        onGoingToAddNew = { toAddNew() },
                        onGoingToLogin = { toLogin() },
                    )
                }
            }
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
        onGoingToTest: () -> (Unit),
        onGoingToLearn: () -> (Unit),
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
            todayAction(text = "$todayTest осталось повторить", onGoingToLearn)
            todayAction(text = "$todayLearn слов осталось изучить", onGoingToTest)
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

// Вынести в другое активити
    /*@Composable
    fun createGrid(datesCount: List<StringInt>) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            contentPadding = PaddingValues(0.dp, 15.dp, 0.dp, 15.dp)
        ) {
            item(span = { GridItemSpan(1) }) {
                createOptionCard()
            }
            item(span = { GridItemSpan(1) }) {
                createAddCard()
            }
            datesCount.forEachIndexed { index, data ->
                if (index == 0) {
                    data.string += " (сегодня)"
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        createListCard(
                            dateCount = data,
                        )
                    }
                } else item(span = { GridItemSpan(1) }) {
                    createListCard(
                        dateCount = data,
                    )
                }

            }
        }
    }*/

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


    @Composable
    fun createOptionCard() {

    }

    /*@OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun createListCard(
        dateCount: StringInt,
        //padding: PaddingValues = PaddingValues(top = 20.dp)
    ) {

        Card(shape = RoundedCornerShape(15.dp), elevation = 4.dp,
            //modifier = Modifier
            //.fillMaxWidth()
            //.height(150.dp)
            //.padding(padding)
            onClick = { toPick(dateCount) }) {

            Box(Modifier.padding(20.dp)) {

                Column(
                    Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = dateCount.int.toString(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 0.dp),
                        //style = TextStyle(fontWeight = FontWeight.Bold),
                        fontSize = 40.sp,
                        color = colorResource(id = R.color.main)
                    )
                    Text(
                        text = dateCount.string,
                        Modifier
                            .alpha(0.5f)
                            .padding(top = 5.dp, bottom = 0.dp),
                        fontSize = 20.sp,

                        )
                }
            }
        }
    }*/

    override fun onResume() {
        super.onResume()
        vm.updateColorScheme()
    }

    fun toLogin() {
        val intent = Intent(this@MainActivity, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun toTest() {
        Log.d(TAG, "toTest")
    }

    fun toLearn() {
        Log.d(TAG, "toLearn")
    }

    fun toPick(dateCount: StringInt) {
        val intent = Intent(this@MainActivity, PickActivity::class.java)
        intent.putExtra("date", dateCount.string)
        //intent.putExtra("count", dateCount.string)
        startActivity(intent)
    }

    fun toSettings() {
        val intent = Intent(this@MainActivity, SettingsActivity::class.java)
        startActivity(intent)
    }

    fun toAddNew() {
        val intent = Intent(this@MainActivity, AddActivity::class.java)
        startActivity(intent)
    }
}