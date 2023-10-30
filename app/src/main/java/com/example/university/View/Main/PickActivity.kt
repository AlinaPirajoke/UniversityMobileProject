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
import androidx.compose.material.Card
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.university.Model.DBManager
import com.example.university.theme.mainColor
import com.example.university.usefull_stuff.formatDate
import com.example.university.usefull_stuff.simpleFormatterWithYear
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
        val db = DBManager(this)
        val count = db.getSizeFromDate(dateString, user)
        /////

        setContent() {
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
                    backgroundColor = mainColor,
                ) {
                    Column(
                        Modifier.fillMaxSize().padding(20.dp, 30.dp),
                        verticalArrangement = Arrangement.SpaceAround) {
                        var pickedCount by remember { mutableStateOf((count?.div(2))) }

                        Text(
                            text = "Выберите количество слов, которое хотите повторить",
                            fontSize = 20.sp,
                            color = androidx.compose.ui.graphics.Color.White
                        )

                        Text(text = "$pickedCount из $count", fontSize = 30.sp, color = androidx.compose.ui.graphics.Color.White)
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
                            Text(text = "Выбрать 5", color = androidx.compose.ui.graphics.Color.White, fontSize = 20.sp)
                            Text(text = "Выбрать 10", color = androidx.compose.ui.graphics.Color.White, fontSize = 20.sp)
                            Text(text = "Выбрать 15", color = androidx.compose.ui.graphics.Color.White, fontSize = 20.sp)
                        }
                    }

                }

            }
        }
    }
}