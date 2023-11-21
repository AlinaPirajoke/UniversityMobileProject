package com.example.university.View.Main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.university.Model.AppDB.AppDbManager
import com.example.university.theme.ColorScheme
import com.example.university.theme.KotobaCustomTheme
import com.example.university.UsefullStuff.formatDate
import com.example.university.UsefullStuff.simpleFormatterWithYear
import java.time.LocalDate

class PickActivity : AppCompatActivity() {
    val TAG = "PickActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this /* Activity context */)
        val user = sharedPreferences.getInt("user", 0)
        val extras = intent.extras
        var dateString = extras?.getString("date")
        dateString = dateString?.replace(" (сегодня)", "")
        val date = LocalDate.parse(
            dateString + " " + LocalDate.now().year.toString(),
            simpleFormatterWithYear
        )
        dateString = formatDate(date)
        Log.i(TAG, "Выбранная дата: ${dateString}")

        // not MVVM
        val db = AppDbManager(this)
        val count = db.getQuantityFromDate(dateString, user)
        /////

        setContent() {
            KotobaCustomTheme(colorScheme = ColorScheme.PH.colors) {

                Column(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    Card(
                        Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        shape = RoundedCornerShape(0.dp, 0.dp, 30.dp, 30.dp),
                        elevation = 4.dp,
                        backgroundColor = MaterialTheme.colors.primary,
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp, 30.dp),
                            verticalArrangement = Arrangement.SpaceAround
                        ) {
                            var pickedCount by remember { mutableStateOf((count?.div(2))) }

                            Text(
                                text = "Выберите количество слов, которое хотите повторить",
                                fontSize = 20.sp,
                                color = MaterialTheme.colors.onPrimary,
                            )

                            Text(
                                text = "$pickedCount из $count",
                                fontSize = 30.sp,
                                color = MaterialTheme.colors.onPrimary,
                            )
                            Slider(
                                value = pickedCount?.toFloat()!!,
                                onValueChange = {
                                    pickedCount = it.toInt()
                                },
                                valueRange = 0f..count?.toFloat()!!
                            )

                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Выбрать 5",
                                    color = MaterialTheme.colors.onPrimary,
                                    fontSize = 20.sp
                                )
                                Text(
                                    text = "Выбрать 10",
                                    color = MaterialTheme.colors.onPrimary,
                                    fontSize = 20.sp
                                )
                                Text(
                                    text = "Выбрать 15",
                                    color = MaterialTheme.colors.onPrimary,
                                    fontSize = 20.sp
                                )
                            }
                        }

                    }
                    Column (
                        Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ){
                        Button(onClick = { /*TODO*/ }) {
                            Text(text = "Сюды")
                        }
                        Button(onClick = { /*TODO*/ }) {
                            Text(text = "Туды")
                        }
                    }
                }
            }
        }
    }
}