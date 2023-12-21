package com.example.university.model.appDB

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.university.model.MySharedPreferences
import com.example.university.usefullStuff.Word
import com.example.university.usefullStuff.getDateNDaysLater
import com.example.university.usefullStuff.getDaysFromToday
import com.example.university.usefullStuff.getTodayDate
import com.example.university.usefullStuff.stdFormatter

class AppDbManager(val context: Context, val msp: MySharedPreferences, val dbHelper: AppDbHelper) { // оставь надежду всяк сюда входящий
    val TAG = "DBManager"
    var db: SQLiteDatabase? = null

    init {
        db = dbHelper.writableDatabase
        if (db == null) Log.e(TAG, "Ошибка открытия бд")
        else {
            Log.d(TAG, "бд открыто")
            //checkWord()
        }
    }

    fun fillWordsLibrary(words: ArrayList<Word>) {
        words.forEach { word ->
            val values = ContentValues().apply {
                put(AppDbNames.LI_WORD, word.word)
                put(AppDbNames.LI_SOUND, word.transcription)
            }
            db!!.insert(AppDbNames.LIBRARY, null, values)
            val cursor = db!!.rawQuery(
                "SELECT MAX(${AppDbNames.LI_WORD_ID}) FROM ${AppDbNames.LIBRARY}",
                null
            )
            cursor?.moveToFirst()
            val word_id = cursor?.getInt(0)!!
            cursor.close()

            addNewTranslToLibrary(word_id, word.translations)
        }
        Log.i(TAG, "${words.size} слов было добавленно в библиотеку")
    }

    fun addNewTranslToLibrary(wordId: Int, transl: List<String>) {
        transl.forEach {
            val values = ContentValues().apply {
                put(AppDbNames.LIT_WORD, wordId)
                put(AppDbNames.LIT_TRANSL, it)
            }
            db?.insert(AppDbNames.LI_TRANSLATION, null, values)
        }
    }

    fun getUnlernedLibraryWords(user: Int): ArrayList<Word> {
        val words = ArrayList<Word>()
        val cursor = db!!.rawQuery(
            // "SELECT ${AppDbNames.LIBRARY}.${AppDbNames.LI_WORD_ID}, ${AppDbNames.LIBRARY}.${AppDbNames.LI_WORD}, ${AppDbNames.LIBRARY}.${AppDbNames.LI_SOUND} FROM ${AppDbNames.LIBRARY} JOIN ${AppDbNames.LIBRARY_USER} ON ${AppDbNames.LIBRARY}.${AppDbNames.LI_WORD_ID} = ${AppDbNames.LIBRARY_USER}.${AppDbNames.LIU_WORD_ID} WHERE ${AppDbNames.LIBRARY}.${AppDbNames.LI_WORD_ID} NOT IN (SELECT ${AppDbNames.LIU_WORD_ID} FROM ${AppDbNames.LIBRARY_USER} WHERE ${AppDbNames.LIU_USER} <> $user)",
            "SELECT ${AppDbNames.LI_WORD_ID}, ${AppDbNames.LI_WORD}, ${AppDbNames.LI_SOUND} FROM ${AppDbNames.LIBRARY} WHERE ${AppDbNames.LI_WORD_ID} NOT IN (SELECT ${AppDbNames.LIU_WORD_ID} FROM ${AppDbNames.LIBRARY_USER} WHERE ${AppDbNames.LIU_USER} = $user)",
            null
        )

        while (cursor.moveToNext()) {
            val id = cursor?.getInt(0)!!
            val word = cursor.getString(1).toString()
            val transcr = cursor.getString(2).toString()
            val lvl = 0
            val transl = getTranslationsFromLibrary(id)

            val resultWord = Word(
                id = id, word = word, transcription = transcr, translations = transl, lvl = lvl
            )
            words.add(resultWord)
        }
        cursor?.close()
        return words
    }

    fun getTranslationsFromLibrary(wordId: Int): ArrayList<String> {
        val transl = ArrayList<String>()
        val cursor = db!!.rawQuery(
            "SELECT ${AppDbNames.LIT_TRANSL} FROM ${AppDbNames.LI_TRANSLATION} WHERE ${AppDbNames.LIT_WORD} = $wordId",
            null
        )

        while (cursor.moveToNext()) {
            transl.add(cursor?.getString(0).toString())
        }
        cursor.close()
        return transl
    }

    fun markWordsAsLearned(picked: List<Word>, user: Int) {
        picked.forEach { word ->
            val values = ContentValues().apply {
                put(AppDbNames.LIU_WORD_ID, word.id)
                put(AppDbNames.LIU_USER, user)
            }
            db!!.insert(AppDbNames.LIBRARY_USER, null, values)
            Log.i(TAG, "Слово ${word.word} было отмечено как изучаемое")
        }
    }

    fun insertPassword(pass: String) {
        val values = ContentValues().apply {
            put(AppDbNames.P_PASS, pass)
        }
        db!!.insert(AppDbNames.PASSWORD, null, values)
    }

    fun getPasswords(): HashMap<String, Int> {
        Log.i(TAG, "Пользователи:")
        val values = HashMap<String, Int>()
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

    fun getListsSizeAndDays(length: Int, user: Int): List<Pair<String, Int>> {
        val datesCount = ArrayList<Pair<String, Int>>()
        val dates = getDaysFromToday(length)
        for (date in dates) {
            val sDate = date.format(stdFormatter)
            val quantity = getQuantityFromDate(sDate, user)!!
            datesCount.add(sDate to quantity)
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

    fun getAllWordsQuantity(): Int {
        val cursor = db!!.rawQuery(
            "SELECT COUNT(${AppDbNames.W_DATE}) FROM ${AppDbNames.WORD}",
            null
        )
        cursor.moveToFirst()
        val quantity = cursor?.getInt(0)!!
        cursor.close()
        return quantity
    }

    fun getWordsFromDate(date: String, user: Int): ArrayList<Word> {
        val words = ArrayList<Word>()
        val cursor = db!!.rawQuery(
            "SELECT ${AppDbNames.W_ID}, ${AppDbNames.W_WORD}, ${AppDbNames.W_SOUND}, ${AppDbNames.W_LVL} " + "FROM ${AppDbNames.WORD} WHERE ${AppDbNames.W_DATE} = \"$date\" AND ${AppDbNames.W_USER} = \"$user\"",
            null
        )

        while (cursor.moveToNext()) {
            val id = cursor?.getInt(0)
            val word = cursor?.getString(1).toString()
            val transcr = cursor?.getString(2).toString()
            val lvl = cursor?.getInt(3)
            val transl = getTranslationsFromWord(id!!)

            val resultWord = Word(
                id = id, word = word, transcription = transcr, translations = transl, lvl = lvl!!
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
            "SELECT MAX(${AppDbNames.W_ID}) FROM ${AppDbNames.WORD} WHERE ${AppDbNames.W_WORD} = \"$word\" AND ${AppDbNames.W_LVL} = $days AND ${AppDbNames.W_USER} = ${user}",
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

    fun addNewWord(word: Word, user: Int) {
        addNewWord(word.word, word.transcription, word.translations, word.lvl, user)
    }

    fun addNewTranslations(wordId: Int, transl: List<String>) {
        transl.forEach {
            val values = ContentValues().apply {
                put(AppDbNames.T_WORD, wordId)
                put(AppDbNames.T_TRANSL, it)
            }
            db!!.insert(AppDbNames.TRANSLATION, null, values)
            Log.i(TAG, "Слово было добавленно под номером $wordId")
        }
    }

    fun createList(words: List<Word>, date: String, user: Int): Int {
        // Получаем максимальный id из таблицы со списками
        var listId = 0
        try {
            val cursor = db!!.rawQuery(
                "SELECT MAX(${AppDbNames.L_ID}) FROM ${AppDbNames.LIST}", null
            )
            cursor.moveToFirst()
            listId = cursor.getInt(0) + 1
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

    fun logAllWords() { // Оно нужно для дебага
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

    fun addNewWords(words: List<Word>, user: Int) {
        words.forEach {
            addNewWord(it, user)
        }
    }

    fun getAllWords(user: Int): ArrayList<Word> {
        val words = ArrayList<Word>()
        val cursor = db!!.rawQuery(
            "SELECT ${AppDbNames.W_ID}, ${AppDbNames.W_WORD}, ${AppDbNames.W_SOUND}, ${AppDbNames.W_LVL}, ${AppDbNames.W_DATE} " + "FROM ${AppDbNames.WORD} WHERE ${AppDbNames.W_USER} = $user",
            null
        )

        while (cursor.moveToNext()) {
            val id = cursor?.getInt(0)!!
            val word = cursor.getString(1).toString()
            val transcr = cursor.getString(2).toString()
            val lvl = cursor.getInt(3)
            val date = cursor.getString(4).toString()
            val transl = getTranslationsFromWord(id)

            val tempWord = Word(
                id = id,
                word = word,
                transcription = transcr,
                translations = transl,
                lvl = lvl
            )
            tempWord.comming = date
            words.add(
                tempWord
            )
        }
        cursor.close()
        return words
    }

    fun deleteWord(word: Word) = deleteWord(word.id)

    fun deleteWord(id: Int) {
        db?.execSQL("DELETE FROM ${AppDbNames.WORD} WHERE ${AppDbNames.W_ID} = $id")
    }
}