package com.example.university.model.wordsDB

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.university.usefullStuff.Word

class WordsDbManager(val context: Context, val dbHelper: WordsDbHelper) {
    val TAG = "WordsDBManager"
    var db: SQLiteDatabase = dbHelper.readableDatabase

    fun getAllWords(): ArrayList<Word> {
        Log.i(TAG, "Начинается перемещение библиотеки в базу данных")
        val words = ArrayList<Word>()
        val cursor = db.rawQuery(
            "SELECT ${WordsDbNames.W_ID}, ${WordsDbNames.W_WORD}, ${WordsDbNames.W_TRANSCR} FROM ${WordsDbNames.WORD}",
            null
        )

        while (cursor.moveToNext()) {
            val id = cursor?.getInt(0)
            val word = cursor?.getString(1).toString()
            val transcr = cursor?.getString(2).toString()
            val transl = getTranslationsFromWord(id!!)

            val resultWord = Word(
                id = id, word = word, transcription = transcr, translations = transl, lvl = 0
            )
            words.add(resultWord)
        }
        Log.i(
            TAG, "Всего слов в библиотеке: ${words.size}"
        )
        cursor.close()
        return words
    }

    fun getTranslationsFromWord(wordId: Int): ArrayList<String> {
        val transl = ArrayList<String>()
        val cursor = db.rawQuery(
            "SELECT ${WordsDbNames.TRANSLATION}.${WordsDbNames.T_TRANSL} FROM ${WordsDbNames.TRANSLATION} JOIN ${WordsDbNames.WORD} ON ${WordsDbNames.TRANSLATION}.${WordsDbNames.T_WORD} = ${WordsDbNames.WORD}.${WordsDbNames.W_ID} WHERE ${WordsDbNames.WORD}.${WordsDbNames.W_ID} = $wordId",
            null
        )
        while (cursor.moveToNext()) {
            transl.add(cursor?.getString(0).toString())
        }
        cursor.close()
        return transl
    }
}