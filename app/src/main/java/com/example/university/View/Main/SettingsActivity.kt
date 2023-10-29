package com.example.university.View.Main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.university.Model.DBManager
import com.example.university.R
import com.example.university.usefull_stuff.showToast

class SettingsActivity : AppCompatActivity() {
    val TAG = "SettingsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = DBManager(this)
        val context = this
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)
        setContent {
            val mainColor = colorResource(id = R.color.main)
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(20.dp, 10.dp),
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Настройки", fontSize = 20.sp,)

                    Button(
                        onClick = {
                            toMain()
                        },
                        modifier = Modifier.width(125.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = mainColor,
                            contentColor = Color.White
                        )
                    ) {

                        Text(text = "Выйти")
                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(15.dp)
                ) {
                    item {
                        switchOption(
                            currentState = sharedPreferences.getBoolean("needPassword", false),
                            descr = "Вход через пароль",
                            action = ::setNeedPassword,
                            context = context,
                            db = db
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun switchOption(
        currentState: Boolean,
        descr: String,
        action: (Boolean, Context, DBManager) -> Boolean,
        context: Context,
        db: DBManager
    ) {
        val mainColor = colorResource(id = R.color.main)
        var checked by remember { mutableStateOf(currentState) }

        Row(Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {

            Switch(
                checked = checked,
                onCheckedChange = {
                    Log.d(TAG, "Попытка установить настройку \"$descr\" в состояние $it")
                    checked = action(it, context, db)
                    if (checked == it)
                        Log.d(TAG, "Разрешено")
                    else
                        Log.w(TAG, "Отклонено")

                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = colorResource(id = R.color.mainLight),
                    checkedTrackColor = mainColor,
                    uncheckedThumbColor =  colorResource(id = R.color.light),
                    uncheckedTrackColor = colorResource(id = R.color.grey)
                ),
                modifier = Modifier.padding(end = 10.dp)
            )

            Text(text = descr, fontSize = 15.sp)
        }
    }

    fun setNeedPassword(state: Boolean, context: Context, db: DBManager): Boolean {
        if (db.getPasswords().size > 1){
            showToast("Для изменения этого параметра, пользователь должен быть только один!", context)
            Log.w(TAG, "Отклонено")
            return !state
        }
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.edit().putBoolean("needPassword", state).apply()
        return state
    }

    fun toMain() {
        finish()
    }
}