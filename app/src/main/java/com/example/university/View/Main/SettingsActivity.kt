package com.example.university.View.Main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.university.Model.DBManager
import com.example.university.R
import com.example.university.ViewModel.SettingsViewModel
import com.example.university.theme.ColorScheme
import com.example.university.theme.KotobaCustomTheme
import com.example.university.theme.PH
import com.example.university.usefull_stuff.showToast
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    val TAG = "SettingsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vm by viewModel<SettingsViewModel>()

        setContent {
            KotobaCustomTheme(colorScheme = ColorScheme.PH.colors) {
                val uiState by vm.uiState.collectAsState()
                settingsView(
                    isPassNeeded = uiState.isPasswordNeeded,
                    onIsPassNeededChange = { vm.setIsPasswordNeeded(it) },
                    currentColorScheme = uiState.currentColorScheme,
                    onColorSchemeChange = { vm.setCurrentColorScheme(it) }
                )
            }
        }
    }

    @Composable
    fun settingsView(
        isPassNeeded: Boolean,
        onIsPassNeededChange: (Boolean) -> (Unit),
        currentColorScheme: Int,
        onColorSchemeChange: (Int) -> (Unit),
    ) {
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
                Text(text = "Настройки", fontSize = 20.sp)

                Button(
                    onClick = {
                        toMain()
                    },
                    modifier = Modifier.width(125.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primary,
                        contentColor = MaterialTheme.colors.onPrimary
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
                    Column(Modifier.fillMaxWidth()) {
                        switchOption(
                            currentState = isPassNeeded,
                            description = "Вход через пароль",
                            action = onIsPassNeededChange,
                        )
                    }
                }
                item {
                    Text(
                        text = "Цветовая схема приложения",
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.h6
                    )
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // PH тема
                        colorTile(bcColor = PH, onClick = {onColorSchemeChange(0)})
                        // pink тема
                        colorTile(bcColor = PH, onClick = {onColorSchemeChange(1)})
                    }
                }
            }
        }
    }

    @Composable
    private fun switchOption(
        currentState: Boolean,
        description: String,
        action: (Boolean) -> (Unit),
    ) {
        val mainColor = colorResource(id = R.color.main)
        var checked by remember { mutableStateOf(currentState) }

        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Switch(
                checked = checked,
                onCheckedChange = {
                    Log.d(TAG, "Попытка установить настройку \"$description\" в состояние $it")
                    action(it)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = colorResource(id = R.color.mainLight),
                    checkedTrackColor = mainColor,
                    uncheckedThumbColor = colorResource(id = R.color.light),
                    uncheckedTrackColor = colorResource(id = R.color.grey)
                ),
                modifier = Modifier.padding(end = 10.dp)
            )

            Text(text = description, style = MaterialTheme.typography.subtitle1)
        }
    }

    @Composable
    fun colorTile(
        bcColor: Color,
        onClick: () -> (Unit),
    ){
        Surface(
            Modifier
                .size(width = 40.dp, height = 40.dp)
                .clickable { onClick() },
            shape = RoundedCornerShape(10.dp),
            color = bcColor,
            elevation = 4.dp
        ){}
    }

    fun toMain() {
        finish()
    }
}