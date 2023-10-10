package com.example.university.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.university.usefull_stuff.StringInt
import com.example.university.usefull_stuff.formatDate
import com.example.university.usefull_stuff.getDateNDaysLater
import com.example.university.usefull_stuff.getDaysFromToday
import com.example.university.usefull_stuff.getTodayDate
import com.example.university.usefull_stuff.simpleFormatter
import java.time.LocalDate


class DBManager (val context: Context){
    val TAG = "DBManager"
    val dbHelper = DBHelper(context)
    var db: SQLiteDatabase? = null

    init {
        db = dbHelper.writableDatabase
        if(db == null) Log.e(TAG, "Ошибка открытия бд")
        else{
            Log.d(TAG, "бд открыто")
            dailyDateUpdate()
            checkWord()
        }
    }

    fun checkWord(){
        Log.d(TAG, "Проверка дб:")
        val cursor = db?.rawQuery("SELECT ${DBNames.W_DATE} FROM ${DBNames.WORD}", null)
        while (cursor?.moveToNext() == true)
            Log.i(TAG, "${cursor.getString(0)}")
    }

    fun insertPassword(pass: String){
        val values = ContentValues().apply {
            put(DBNames.P_PASS, pass)
        }
        db?.insert(DBNames.PASSWORD, null, values)
    }

    fun getPasswords(): HashMap<String, Int>{
        Log.i(TAG, "Пользователи:")
        var values = HashMap<String, Int>()
        val cursor = db?.rawQuery("SELECT ${DBNames.P_PASS}, ${DBNames.P_ID} " +
                "FROM ${DBNames.PASSWORD}", null)
        while (cursor?.moveToNext()!!){
            val pass = cursor.getString(0)
            val id = cursor.getInt(1)
            values.put(pass, id)
            Log.i(TAG, "$pass, $id")
        }
        return values
    }

    fun getListsSizeAndDays(n: Int, user: Int): ArrayList<StringInt>{
        val datesCount = ArrayList<StringInt>()
        val dates = getDaysFromToday(n)
        for(date in dates){
            val dt = date.format(simpleFormatter)
            val count = getSizeFromDate(date, user)
            count?.let { StringInt(dt, it) }?.let { datesCount.add(it) }
        }
        return datesCount
    }

    fun dailyDateUpdate(){
        val now = getTodayDate()
        db!!.execSQL("UPDATE ${DBNames.WORD} SET ${DBNames.W_DATE} = $now WHERE ${DBNames.W_DATE} < $now")
    }

    fun getSizeFromDate(date: LocalDate, user: Int): Int? {
        val sDate = formatDate(date)
        val cursor = db?.rawQuery("SELECT COUNT(*) FROM ${DBNames.WORD} WHERE ${DBNames.W_DATE} like \"$sDate\" AND ${DBNames.W_USER} = $user", null)
        cursor?.moveToFirst()
        val size = cursor?.getInt(0)
        return size
    }

    fun addNewWord(enWord: String, transc: String, ruWord: String, days: Int, user: Int){
        val date = getDateNDaysLater(days)
        val values = ContentValues().apply {
            put(DBNames.W_WORD, enWord)
            put(DBNames.W_SOUND, transc)
            put(DBNames.W_TRNSL, ruWord)
            put(DBNames.W_LVL, days)
            put(DBNames.W_DATE, date)
            put(DBNames.W_USER, user)
        }
        db?.insert(DBNames.WORD, null, values)
    }
}