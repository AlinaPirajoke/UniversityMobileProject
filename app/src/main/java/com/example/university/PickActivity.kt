package com.example.university

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.university.database.DBManager
import com.example.university.usefull_stuff.formatDate
import com.example.university.usefull_stuff.simpleFormatter
import com.example.university.usefull_stuff.simpleFormatterWithYear
import com.example.university.usefull_stuff.stdFormatter
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
        val date = LocalDate.parse(dateString + " " + LocalDate.now().year.toString(), simpleFormatterWithYear)
        Log.i(TAG, "Выбранная дата: ${formatDate(date)}")

        // not MVVM
        val db = DBManager(this)
        val count = db.getSizeFromDate(date, user)
        /////

        setContent() {
            Column(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(20.dp, 10.dp)
            ) {
                Text(text = "Выберите количество слов, которые хотите пройти", fontSize = 20.sp)

                var pickedCount by remember { mutableStateOf((count?.div(2))) }
                Text(text = "$pickedCount из $count", fontSize = 20.sp)
                Slider(
                    value = pickedCount?.toFloat()!!,
                    onValueChange = {
                        pickedCount = it.toInt()

                    },
                    valueRange = 0f..count?.toFloat()!!)
            }
        }
    }
}