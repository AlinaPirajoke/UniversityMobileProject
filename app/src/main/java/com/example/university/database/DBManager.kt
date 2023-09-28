package com.example.university.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.university.usefull_stuff.StringInt
import com.example.university.usefull_stuff.getDaysFromToday
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.random.Random


class DBManager (val context: Context){
    val TAG = "DBManager"
    val dbHelper = DBHelper(context)
    var db: SQLiteDatabase? = null

    init {
        db = dbHelper.writableDatabase
        if(db == null) Log.e(TAG, "Ошибка открытия бд")
    }

    fun insertPassword(pass: String){
        val values = ContentValues().apply {
            put(DBNames.P_PASS, pass)
        }
        db?.insert(DBNames.TABLE_PASSWORD, null, values)
    }

    fun getPasswords(): HashMap<String, Int>{
        var values = HashMap<String, Int>()
        val cursor = db?.rawQuery("SELECT ${DBNames.P_PASS}, ${DBNames.P_ID} " +
                "FROM ${DBNames.TABLE_PASSWORD}", null)
        while (cursor?.moveToNext()!!){
            val pass = cursor.getString(0)
            val id = cursor.getInt(1)
            values.put(pass, id)
        }
        return values
    }

    fun getListsSizeAndDays(n: Int): List<StringInt>{
        val datesCount = ArrayList<StringInt>()
        val dates = getDaysFromToday(n)
        val formatter = DateTimeFormatter.ofPattern("d MMMM", Locale("ru"))
        for(date in dates){
            val dt = date.format(formatter)
            val count = getSizeFromDate(date)
            datesCount.add(StringInt(dt, count))
        }
        return datesCount
    }

    fun getSizeFromDate(date: LocalDate): Int{
        return Random(40).nextInt()%40
    }
}