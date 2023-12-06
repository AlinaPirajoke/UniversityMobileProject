package com.example.university.Model.AppDB

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.university.Model.MySharedPreferences
import com.example.university.UsefullStuff.StringInt
import com.example.university.UsefullStuff.Word
import com.example.university.UsefullStuff.getDateNDaysLater
import com.example.university.UsefullStuff.getDaysFromToday
import com.example.university.UsefullStuff.getTodayDate
import com.example.university.UsefullStuff.simpleFormatter

class AppDbManager(val context: Context) {
    val TAG = "DBManager"
    val dbHelper = AppDbHelper(context)
    var db: SQLiteDatabase? = null
    val msp = MySharedPreferences(context)

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
        while (cursor?.moveToNext() == true) Log.i(TAG, "${cursor.getString(0)}")
        cursor.close()
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
            "SELECT ${AppDbNames.P_PASS}, ${AppDbNames.P_ID} " + "FROM ${AppDbNames.PASSWORD}", null
        )
        while (cursor?.moveToNext()!!) {
            val pass = cursor.getString(0)
            val id = cursor.getInt(1)
            values.put(pass, id)
            Log.i(TAG, "$pass, $id")
        }
        cursor.close()
        return values
    }

    fun getListsSizeAndDays(n: Int, user: Int): ArrayList<StringInt> {
        val datesCount = ArrayList<StringInt>()
        val dates = getDaysFromToday(n)
        for (date in dates) {
            val dt = date.format(simpleFormatter)
            val count = getQuantityFromDate(dt, user)
            count?.let { StringInt(dt, it) }?.let { datesCount.add(it) }
        }
        return datesCount
    }

    fun getQuantityFromDate(date: String, user: Int): Int? {
        val cursor = db!!.rawQuery(
            "SELECT COUNT(*) FROM ${AppDbNames.WORD} WHERE ${AppDbNames.W_DATE} = \"$date\" AND ${AppDbNames.W_USER} = $user",
            null
        )
        cursor?.moveToFirst()
        val size = cursor?.getInt(0)
        cursor.close()
        return size
    }

    fun dailyDateUpdate() {
        val now = getTodayDate()
        val cursor = db!!.rawQuery(
            "SELECT COUNT(*) FROM ${AppDbNames.WORD} WHERE ${AppDbNames.W_DATE} < \"$now\"", null
        )
        cursor?.moveToFirst()
        val number = cursor?.getInt(0)
        db!!.execSQL("UPDATE ${AppDbNames.WORD} SET ${AppDbNames.W_DATE} = \"$now\" WHERE ${AppDbNames.W_DATE} < \"$now\"")
        Log.i(TAG, "Обновлена дата $number записей")
        cursor.close()
    }

    // Доделать
    fun getTodayLearnedCount(date: String, user: Int): Int? {
        return 0
    }

    fun getLearnedCount(): Int {
        val cursor = db!!.rawQuery(
            "SELECT COUNT(*) FROM ${AppDbNames.WORD} WHERE ${AppDbNames.W_LVL} < 50", null
        )
        cursor?.moveToFirst()
        val size = cursor?.getInt(0)
        cursor.close()
        return size!!
    }

    fun getLearningCount(): Int {
        val cursor = db!!.rawQuery(
            "SELECT COUNT(*) FROM ${AppDbNames.WORD} WHERE ${AppDbNames.W_LVL} >= 50", null
        )
        cursor?.moveToFirst()
        val size = cursor?.getInt(0)
        cursor.close()
        return size!!
    }

    // Доделать
    fun getAverage(): Int {
        return 7
    }

    fun getWordsFromDate(date: String, user: Int): ArrayList<Word> {
        val words = ArrayList<Word>()
        val cursor = db!!.rawQuery(
            "SELECT ${AppDbNames.W_ID}, ${AppDbNames.W_WORD}, ${AppDbNames.W_SOUND}, ${AppDbNames.W_LVL} " + "FROM ${AppDbNames.WORD} WHERE ${AppDbNames.W_DATE} = \"$date\"",
            null
        )

        while (cursor.moveToNext()) {
            val id = cursor?.getInt(0)
            val word = cursor?.getString(1).toString()
            val transcr = cursor?.getString(2).toString()
            val lvl = cursor?.getInt(3)
            val transl = getTranslationsFromWord(id!!)

            val resultWord = Word(
                id = id!!, word = word, transcription = transcr, translations = transl, lvl = lvl!!
            )
            words.add(resultWord)
        }
        Log.i(
            TAG,
            "Было найдено ${words.size} записей на дату $date: ${words.joinToString { it.word }}"
        )
        cursor.close()
        return words
    }

    fun getTranslationsFromWord(wordId: Int): ArrayList<String> {
        val transl = ArrayList<String>()
        val cursor = db!!.rawQuery(
            "SELECT ${AppDbNames.TRANSLATION}.${AppDbNames.T_TRANSL} FROM ${AppDbNames.TRANSLATION} JOIN ${AppDbNames.WORD} ON ${AppDbNames.TRANSLATION}.${AppDbNames.T_WORD} = ${AppDbNames.WORD}.${AppDbNames.W_ID} WHERE ${AppDbNames.WORD}.${AppDbNames.W_ID} = $wordId",
            null
        )

        while (cursor.moveToNext()) {
            transl.add(cursor?.getString(0).toString())
        }
        cursor.close()
        return transl
    }

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
            "SELECT ${AppDbNames.W_ID} FROM ${AppDbNames.WORD} WHERE ${AppDbNames.W_WORD} = \"$word\" AND ${AppDbNames.W_LVL} = $days AND ${AppDbNames.W_USER} = ${user}",
            null
        )
        cursor?.moveToFirst()
        val word_id = cursor?.getInt(0)
        if (word_id != null) {
            addNewTranslations(wordId = word_id, transl = transl)
        } else Log.e(TAG, "Мы проебали id для слова $word")
        cursor.close()
        msp.todayStudiedQuantity++
    }

    fun addNewWord (word: Word , user: Int){
        addNewWord(word.word, word.transcription, word.translations, word.lvl, user)
    }

    fun addNewTranslations(wordId: Int, transl: List<String>) {
        val values = ContentValues().apply {
            put(AppDbNames.T_WORD, wordId)
            transl.forEach {
                put(AppDbNames.T_TRANSL, it)
            }
        }
        db!!.insert(AppDbNames.TRANSLATION, null, values)
        Log.i(TAG, "Слово было добавленно под номером $wordId")
    }

    fun createList(words: List<Word>, date: String, user: Int): Int {
        // Получаем максимальный id из таблицы со списками
        var listId = 0
        try {
            val cursor = db!!.rawQuery(
                "SELECT MAX(${AppDbNames.L_ID}) FROM ${AppDbNames.LIST}", null
            )
            cursor.moveToFirst()
            listId = cursor.getInt(0)!! + 1
            cursor.close()
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка добавления: ${e.javaClass}")
        }

        // Создаём под вычисленным listId записи со словами в таблицу со списками
        words.forEach { word ->
            val listValues = ContentValues().apply {
                put(AppDbNames.L_ID, listId)
                put(AppDbNames.L_WORD, word.id)
                put(AppDbNames.L_IS_NEW, if (word.lvl > 0) 1 else 0)
            }
            db!!.insert(AppDbNames.LIST, null, listValues)
        }

        // Заносим даннае о списке в соответствующую таблицу (с данными о списках)
        val dataValues = ContentValues().apply {
            put(AppDbNames.LD_LIST, listId)
            put(AppDbNames.LD_USER, user)
            put(AppDbNames.LD_DATE, date)
        }
        db!!.insert(AppDbNames.LIST_DATA, null, dataValues)

        return listId
    }

    fun getWordsFromList(list: Int): ArrayList<Word> {
        val words = ArrayList<Word>()
        val cursor = db!!.rawQuery(
            "SELECT ${AppDbNames.WORD}.${AppDbNames.W_ID}, ${AppDbNames.WORD}.${AppDbNames.W_WORD}, ${AppDbNames.WORD}.${AppDbNames.W_SOUND}, ${AppDbNames.WORD}.${AppDbNames.W_LVL} FROM ${AppDbNames.WORD} JOIN ${AppDbNames.LIST} ON ${AppDbNames.WORD}.${AppDbNames.W_ID} = ${AppDbNames.LIST}.${AppDbNames.L_WORD} WHERE ${AppDbNames.LIST}.${AppDbNames.L_ID} = $list",
            null
        )

        while (cursor.moveToNext()) {
            val id = cursor?.getInt(0)
            val word = cursor?.getString(1).toString()
            val transcr = cursor?.getString(2).toString()
            val lvl = cursor?.getInt(3)!!
            val transl = getTranslationsFromWord(id!!)

            val resultWord = Word(
                id = id, word = word, transcription = transcr, translations = transl, lvl = lvl
            )
            words.add(resultWord)
        }
        cursor.close()
        return words
    }

    fun saveWordResult(word: Word, listId: Int) {
        db!!.execSQL(
            "UPDATE ${AppDbNames.LIST} SET ${AppDbNames.L_RESULT} = ${word.result}, ${AppDbNames.L_IS_FINISHED} = 1 WHERE ${AppDbNames.L_WORD} = ${word.id} AND ${AppDbNames.L_ID} = $listId"
        )
        Log.i(
            TAG,
            "Резильтат слова ${word.word} записан как ${AppDbNames.L_RESULT} = ${word.result}, ${AppDbNames.L_IS_FINISHED} = 1"
        )
        updateWordDate(word)
    }

    fun updateWordDate(word: Word) {
        db!!.execSQL(
            "UPDATE ${AppDbNames.WORD} SET ${AppDbNames.W_LVL} = ${word.lvl}, ${AppDbNames.W_DATE} = \"${
                getDateNDaysLater(
                    word.lvl
                )
            }\" WHERE ${AppDbNames.W_ID} = ${word.id}"
        )
        Log.i(TAG, "Слово ${word.word} отправлено на дату ${getDateNDaysLater(word.lvl)}")
    }

    fun logAllWords() {
        Log.d(TAG, "Печать всех записей таблицы word:")
        val cursor = db!!.rawQuery(
            "SELECT ${AppDbNames.W_ID}, ${AppDbNames.W_WORD}, ${AppDbNames.W_SOUND}, ${AppDbNames.W_LVL}, ${AppDbNames.W_DATE} " + "FROM ${AppDbNames.WORD}",
            null
        )

        while (cursor.moveToNext()) {
            val id = cursor?.getInt(0)
            val word = cursor?.getString(1).toString()
            val transcr = cursor?.getString(2).toString()
            val lvl = cursor?.getInt(3)
            val date = cursor?.getString(4).toString()
            val transl = getTranslationsFromWord(id!!)

            Log.i(
                TAG,
                "id: $id, date: $date, word: $word, transcr: $transcr, lvl: $lvl, transl: ${transl.joinToString()}"
            )
        }
        cursor.close()
    }

    fun addNewWords(words: List<Word>, user: Int){
        words.forEach{
            addNewWord(it, user)
        }
    }
}