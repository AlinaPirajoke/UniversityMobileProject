package com.example.university.Model.AppDB

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.university.usefull_stuff.StringInt
import com.example.university.usefull_stuff.getDateNDaysLater
import com.example.university.usefull_stuff.getDaysFromToday
import com.example.university.usefull_stuff.getTodayDate
import com.example.university.usefull_stuff.simpleFormatter


class AppDbManager(val context: Context) {
    val TAG = "DBManager"
    val dbHelper = AppDbHelper(context)
    var db: SQLiteDatabase? = null

    init {
        db = dbHelper.writableDatabase
        if (db == null) Log.e(TAG, "Ошибка открытия бд")
        else {
            Log.d(TAG, "бд открыто")
            //checkWord()
        }
    }

    fun checkWord() {
        Log.d(TAG, "Проверка дб:")
        val cursor = db!!.rawQuery("SELECT ${AppDbNames.W_DATE} FROM ${AppDbNames.WORD}", null)
        while (cursor?.moveToNext() == true)
            Log.i(TAG, "${cursor.getString(0)}")
    }

    fun insertPassword(pass: String) {
        val values = ContentValues().apply {
            put(AppDbNames.P_PASS, pass)
        }
        db!!.insert(AppDbNames.PASSWORD, null, values)
    }

    fun getPasswords(): HashMap<String, Int> {
        Log.i(TAG, "Пользователи:")
        var values = HashMap<String, Int>()
        val cursor = db!!.rawQuery(
            "SELECT ${AppDbNames.P_PASS}, ${AppDbNames.P_ID} " +
                    "FROM ${AppDbNames.PASSWORD}", null
        )
        while (cursor?.moveToNext()!!) {
            val pass = cursor.getString(0)
            val id = cursor.getInt(1)
            values.put(pass, id)
            Log.i(TAG, "$pass, $id")
        }
        return values
    }

    fun getListsSizeAndDays(n: Int, user: Int): ArrayList<StringInt> {
        dailyDateUpdate()

        val datesCount = ArrayList<StringInt>()
        val dates = getDaysFromToday(n)
        for (date in dates) {
            val dt = date.format(simpleFormatter)
            val count = getSizeFromDate(dt, user)
            count?.let { StringInt(dt, it) }?.let { datesCount.add(it) }
        }
        return datesCount
    }

    fun dailyDateUpdate() {
        val now = getTodayDate()
        db!!.execSQL("UPDATE ${AppDbNames.WORD} SET ${AppDbNames.W_DATE} = $now WHERE ${AppDbNames.W_DATE} < $now")
    }

    fun getSizeFromDate(date: String, user: Int): Int? {
        val cursor = db!!.rawQuery(
            "SELECT COUNT(*) FROM ${AppDbNames.WORD} WHERE ${AppDbNames.W_DATE} like \"$date\" AND ${AppDbNames.W_USER} = $user",
            null
        )
        cursor?.moveToFirst()
        val size = cursor?.getInt(0)
        return size
    }

    // Доделать
    fun getTodayLearnedCount(date: String, user: Int): Int? {
        return 0
    }

    fun getLearnedCount(): Int {
        val cursor =
            db!!.rawQuery(
                "SELECT COUNT(*) FROM ${AppDbNames.WORD} WHERE ${AppDbNames.W_LVL} < 50",
                null
            )
        cursor?.moveToFirst()
        val size = cursor?.getInt(0)
        return size!!
    }

    fun getLerningCount(): Int {
        val cursor =
            db!!.rawQuery(
                "SELECT COUNT(*) FROM ${AppDbNames.WORD} WHERE ${AppDbNames.W_LVL} >= 50",
                null
            )
        cursor?.moveToFirst()
        val size = cursor?.getInt(0)
        return size!!
    }

    // Доделать
    fun getAverage(): Int {
        return 7
    }

    // Переписать
    fun addNewWord(word: String, transc: String, transl: List<String>, days: Int, user: Int) {
        Log.d(TAG, "Добавление слова $word в бд")
        val date = getDateNDaysLater(days)
        val values = ContentValues().apply {
            put(AppDbNames.W_WORD, word)
            put(AppDbNames.W_SOUND, transc)
            put(AppDbNames.W_LVL, days)
            put(AppDbNames.W_DATE, date)
            put(AppDbNames.W_USER, user)
        }
        db!!.insert(AppDbNames.WORD, null, values)
        Log.d(TAG, "Слово добавлено")
        val cursor = db!!.rawQuery(
            "SELECT ${AppDbNames.W_ID} FROM ${AppDbNames.WORD} WHERE ${AppDbNames.W_WORD} = $word AND ${AppDbNames.W_LVL} = $days AND ${AppDbNames.W_USER} = user",
            null
        )
        cursor?.moveToFirst()
        val word_id = cursor?.getInt(0)
        if (word_id != null) {
            addNewTranslations(wordId = word_id, transl = transl)
        }
        else
            Log.e(TAG, "Мы проебали id для слова $word")
    }

    fun addNewTranslations(wordId: Int, transl: List<String>){
        val values = ContentValues().apply {
            put(AppDbNames.T_WORD, wordId)
            transl.forEach{
                put(AppDbNames.T_TRANSL, it)
            }
        }
        db!!.insert(AppDbNames.TRANSLATION, null, values)
    }
}