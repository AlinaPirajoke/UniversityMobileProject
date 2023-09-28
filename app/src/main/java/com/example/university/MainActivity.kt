package com.example.university

import android.content.Intent
import android.graphics.ColorSpace.Rgb
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.example.university.database.DBManager
import com.example.university.usefull_stuff.StringInt

class MainActivity : AppCompatActivity() {
    val N = 11
    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this /* Activity context */)
        //sharedPreferences.edit().putBoolean("session", false).apply()

        val session = sharedPreferences.getBoolean("session", false)
        if (!session) {
            toLogin()
            finish()
        }
        val user = sharedPreferences.getInt("user", 0)

        super.onCreate(savedInstanceState)
        val db = DBManager(this)
        val lists = db.getListsSizeAndDays(N)

        setContent() {
            val scrollState = rememberScrollState()

            Column(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(20.dp, 0.dp)
                    .verticalScroll(scrollState),

                ) {

                Text(text = "Статус session: ${session}")
                Text(text = "Привет, человек номер${user}")
                Button(onClick = { toLogin() }) {
                    Text(text = "Разлогиниться")
                }

                var i = 0
                createCardForList(dateCount = lists.get(i))
                i++

                while (i < N) {
                    Row(Modifier.fillMaxWidth()) {
                        createCardForList(dateCount = lists.get(i))
                        i++
                        if (i == N-1)
                            createCardForList(
                                dateCount = lists.get(i),
                                PaddingValues(top = 20.dp, bottom = 20.dp)
                            )
                        else if(i < N)
                            createCardForList(dateCount = lists.get(i))
                        i++
                    }
                }

            }
        }
    }


    @Composable
    fun createCardForList(
        dateCount: StringInt,
        padding: PaddingValues = PaddingValues(top = 20.dp)
    ) {

        Card(
            shape = RoundedCornerShape(15.dp),
            elevation = 5.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(padding)
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

                        )
                    Text(
                        text = dateCount.string,
                        Modifier
                            .alpha(0.5f)
                            .padding(bottom = 10.dp),
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
}