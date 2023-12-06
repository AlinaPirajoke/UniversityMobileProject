package com.example.university.Model.WordsDB

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.university.UsefullStuff.Word

class WordsDbManager(val context: Context) {
    val TAG = "WordsDBManager"
    val dbHelper = WordsDbHelper(context)
    var db: SQLiteDatabase = dbHelper.readableDatabase

    fun getUnlearnedWords(): ArrayList<Word> {
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
            TAG,
            "Осталось ${words.size} неизученных слов}"
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

    fun removeWords(words: List<Word>){
        words.forEach{
            db.execSQL("DELETE FROM ${WordsDbNames.WORD} WHERE ${WordsDbNames.W_ID} = ${it.id}")
            db.execSQL("DELETE FROM ${WordsDbNames.TRANSLATION} WHERE ${WordsDbNames.T_WORD} = ${it.id}")
        }
    }

}