package com.example.university

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.university.database.DBManager
import com.example.university.usefull_stuff.StringInt

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    val N = 17

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "Инициализация...")
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this /* Activity context */)
        //sharedPreferences.edit().putBoolean("session", false).apply()

        var session = sharedPreferences.getBoolean("session", false)

        val needPass = sharedPreferences.getBoolean("needPassword", false)
        if (!needPass)
            session = true

        if (!session) {
            toLogin()
            finish()
        }
        val user = sharedPreferences.getInt("user", 1)
        Log.d(TAG, "Прошла проверка на авторизованность пользователя")

        super.onCreate(savedInstanceState)
        Log.d(TAG, "Activity создано")

        val db = DBManager(this)
        val lists = db.getListsSizeAndDays(N, user)


        Log.d(TAG, "Инициализация прошла успешно")

        setContent() {
            val mainColor = colorResource(id = R.color.main)
            //val scrollState = rememberScrollState()
            //val mainColor = colorResource(id = R.color.main)

            Column(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(20.dp, 0.dp)
                //.verticalScroll(scrollState),

            ) {

                Text(text = "Статус session: ${session}")
                Text(text = "Привет, человек номер${user}")
                Button(
                    onClick = { toLogin() },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = mainColor,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Разлогиниться")
                }

                Log.d(TAG, "Отрисовка Grid...")
                createGrid(datesCount = lists)
                Log.d(TAG, "Интерфейс отрисован")
            }
        }
    }

    @Composable
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
                } else
                    item(span = { GridItemSpan(1) }) {
                        createListCard(
                            dateCount = data,
                        )
                    }

            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun createImgCard(imgId: Int, descr: String, action: () -> Unit) {
        Card(
            shape = RoundedCornerShape(15.dp),
            elevation = 4.dp,
            //modifier = Modifier
            //.fillMaxWidth()
            //.height(150.dp)
            //.padding(padding)
            onClick = { action.invoke() }
        ) {

            Box(Modifier.padding(20.dp)) {

                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
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
    fun createAddCard() {
        createImgCard(imgId = R.drawable.add, descr = "Внести слово", ::toAddNew)
    }

    @Composable
    fun createOptionCard() {
        createImgCard(imgId = R.drawable.setings, descr = "Настройки", ::toSettings)
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun createListCard(
        dateCount: StringInt,
        //padding: PaddingValues = PaddingValues(top = 20.dp)
    ) {

        Card(
            shape = RoundedCornerShape(15.dp),
            elevation = 4.dp,
            //modifier = Modifier
            //.fillMaxWidth()
            //.height(150.dp)
            //.padding(padding)
            onClick = { toPick(dateCount) }
        ) {

            Box(Modifier.padding(20.dp)) {

                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
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
    }

    fun toLogin() {
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
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