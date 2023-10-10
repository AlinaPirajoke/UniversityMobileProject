package com.example.university

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.university.database.DBManager
import com.example.university.usefull_stuff.showToast

class AddActivity : AppCompatActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this /* Activity context */)
        val user = sharedPreferences.getInt("user", 0)
        val db = DBManager(this)

        setContent() {
            var parentSize by remember {
                mutableStateOf(Size.Zero)
            }
            val context = LocalContext.current
            val keyboardController = LocalSoftwareKeyboardController.current
            val focusManager = LocalFocusManager.current
            val scrollState = rememberScrollState()
            val mainColor = colorResource(id = R.color.main)

            Column(
                Modifier
                    .fillMaxHeight()
                    .padding(50.dp, 0.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Center


            ) {
                Text(
                    text = "Добавьте новое слово",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    textAlign = TextAlign.Center,
                    //style = TextStyle(fontWeight = FontWeight.Bold),
                    fontSize = 20.sp,
                )

                var enWord by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = enWord,
                    onValueChange = { enWord = it },
                    label = { Text("Слово") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = mainColor,
                        unfocusedBorderColor = mainColor,
                        cursorColor = mainColor,
                        focusedLabelColor = Color.Black
                    )
                )

                var transc by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = transc,
                    onValueChange = { transc = it },
                    label = { Text("Транскрипция") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = mainColor,
                        unfocusedBorderColor = mainColor,
                        cursorColor = mainColor,
                        focusedLabelColor = Color.Black
                    )
                )

                var ruWord by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = ruWord,
                    onValueChange = { ruWord = it },
                    label = { Text("Перевод") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = mainColor,
                        unfocusedBorderColor = mainColor,
                        cursorColor = mainColor,
                        focusedLabelColor = Color.Black
                    )
                )

                var days by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = days,
                    onValueChange = { days = it },
                    label = { Text("Период показа") },
                    placeholder = {
                        Text( "0")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Number
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        keyboardController?.hide()
                        addWord(enWord, transc, ruWord, days, db, user, context)
                    }),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = mainColor,
                        unfocusedBorderColor = mainColor,
                        cursorColor = mainColor,
                        focusedLabelColor = Color.Black
                    )
                )

                Button(
                    onClick = {
                        addWord(enWord, transc, ruWord, days, db, user, context)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = mainColor,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Добавить")
                }

                Button(
                    onClick = {
                        toMain()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = mainColor,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Выйти")
                }
            }
        }
    }

    fun addWord(
        enWord: String,
        transc: String,
        ruWord: String,
        days: String,
        db: DBManager,
        user: Int,
        context: Context
    ) {
        if(enWord.isBlank()){
            showToast("Введите слово", context)
            return
        }
        try{
            val period: Int
            if(days.isBlank())
                period = 0
            else
                period = days.toInt()
            db.addNewWord(enWord, transc, ruWord, period, user)
        }
        catch (ex: NumberFormatException) {
            showToast("Период должен быть целочисленным", context)
            return
        }
        catch (ex: Exception) {
            showToast("Ошибка добавления", context)
            return
        }

        toExit()
    }

    fun toExit(){
        val intent = Intent(this@AddActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun toMain(){
        val intent = Intent(this@AddActivity, MainActivity::class.java)
        startActivity(intent)
    }

}