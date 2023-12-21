package com.example.university.model.wordsDB

import android.provider.BaseColumns

object WordsDbNames : BaseColumns {
    // Бд, содержащее все слова
    const val DATABASE_NAME = "unlearned_words_database.sqlite3"
    const val DATABASE_VERSION = 2
    // Таблица со словами
    const val WORD = "word_table"
    const val W_ID = BaseColumns._ID
    const val W_WORD = "word"
    const val W_TRANSCR = "transcription"
    const val CREATE_TABLE_WORD = (
            "CREATE TABLE IF NOT EXISTS ${WORD} (" +
                    "$W_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$W_WORD TEXT NOT NULL, " +
                    "$W_TRANSCR TEXT DEFAULT \"\")")
    const val DELETE_TABLE_WORD = "DROP TABLE IF EXISTS ${WORD}"

    // Таблица с переводами слов
    const val TRANSLATION = "translation"
    const val T_ID = BaseColumns._ID
    const val T_WORD = "word_id"
    const val T_TRANSL = "meaning"
    const val CREATE_TABLE_TRANSLATION = (
            "CREATE TABLE IF NOT EXISTS $TRANSLATION (" +
                    "$T_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$T_WORD TEXT NOT NULL, " +
                    "$T_TRANSL TEXT NOT NULL, " +
                    "FOREIGN KEY ($T_WORD) REFERENCES ${WORD} ($W_ID))")
    const val DELETE_TABLE_TRANSLATION = "DROP TABLE IF EXISTS $TRANSLATION"
}