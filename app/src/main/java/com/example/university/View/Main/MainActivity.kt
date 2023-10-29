package com.example.university.View.Main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.university.R
import com.example.university.View.Auth.AuthActivity
import com.example.university.ViewModel.MainViewModel
import com.example.university.ViewModel.MainViewModelFactory
import com.example.university.theme.grayColor
import com.example.university.theme.mainColor
import com.example.university.usefull_stuff.StringInt

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    val N = 17

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "Инициализация...")

        val vm = ViewModelProvider(this, MainViewModelFactory(this)).get(MainViewModel::class.java)

        vm.isGoingToLogin.observe(this, Observer {
            if (it == true) {
                toLogin()
                finish()
            }
        })

        super.onCreate(savedInstanceState)

        setContent() {
            val scrollState = rememberScrollState()

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(0.dp, 0.dp, 0.dp, 0.dp)
                    .verticalScroll(scrollState),
            ) {
                statisticBlock(vm = vm)
                todayBlock(vm = vm)
                otherBlock(vm = vm)
            }
        }
    }

    @Composable
    fun statisticBlock(vm: MainViewModel) {

        var learned by remember {
            mutableStateOf(0)
        }
        vm.statLearned.observe(this, Observer {
            learned = it
        })

        var learning by remember {
            mutableStateOf(0)
        }
        vm.statLearning.observe(this, Observer {
            learning = it
        })

        var average by remember {
            mutableStateOf(0)
        }
        vm.statAverage.observe(this, Observer {
            average = it
        })

        vm.getStatistic()

        Column {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                text = "Статистика",
                fontSize = 15.sp,
                color = grayColor,
                textAlign = TextAlign.Center
            )
            Card(
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
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
    fun todayBlock(vm: MainViewModel) {
        var todayTest by remember {
            mutableStateOf(0)
        }
        vm.todayTest.observe(this, Observer {
            todayTest = it
        })

        var todayLearn by remember {
            mutableStateOf(0)
        }
        vm.todayLearn.observe(this, Observer {
            todayLearn = it
        })

        Column(
            Modifier
                .fillMaxWidth()
                .padding(20.dp), verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                text = "Сегодня",
                fontSize = 15.sp,
                color = grayColor,
                textAlign = TextAlign.Center
            )
            todayAction(text = "$todayTest слов готовы к повторению", ::toTest)
            todayAction(text = "$todayLearn слов осталось изучить", ::toLearn)
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                text = "Посмотреть другие даты",
                fontSize = 15.sp,
                color = mainColor,
                textAlign = TextAlign.Right
            )
        }
    }

    @Composable
    fun otherBlock(vm: MainViewModel) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp),
            shape = RoundedCornerShape(30.dp, 30.dp, 0.dp, 0.dp),
            elevation = 4.dp,
            backgroundColor = mainColor,
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 40.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp),
            ) {
                /*Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(15.dp),) {}*/
                createImgCard(imgId = R.drawable.setings, descr = "Настройки", ::toSettings)
                createImgCard(imgId = R.drawable.add, descr = "Внести слово", ::toAddNew)
                createImgCard(imgId = R.drawable.logout, descr = "Разлогиниться", ::toLogin)
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
            Text(text = text, fontSize = 20.sp)
            Text(text = value.toString(), fontSize = 20.sp)
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
                color = mainColor,
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

                    Image(
                        painter = painterResource(id = imgId),
                        contentDescription = descr,
                        modifier = Modifier.size(40.dp),
                    )
                    Text(
                        text = descr,
                        Modifier
                            .alpha(0.5f)
                            .padding(top = 5.dp, bottom = 0.dp),
                        fontSize = 20.sp,
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