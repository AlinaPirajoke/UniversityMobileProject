package com.example.university.Model

import android.provider.BaseColumns

object DBNames : BaseColumns {
    var DATABASE_NAME = "words_database.db"
    val DATABASE_VERSION = 2

    // Таблица с пользователями
    val PASSWORD = "password"
    val P_ID = BaseColumns._ID
    val P_PASS = "passwors"
    val CREATE_TABLE_PASSWORD = (
                    "CREATE TABLE IF NOT EXISTS $PASSWORD ( " +
                    "$P_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$P_PASS TEXT," +
                    "PRIMARY KEY ($P_ID))")
    val DELETE_TABLE_PASSWORD = "DROP TABLE IF EXISTS $PASSWORD"

    // Таблица со словами
    val WORD = "word"
    val W_ID = BaseColumns._ID
    val W_WORD = "word"
    val W_SOUND = "sound"
    val W_DATE = "test_date"
    val W_LVL = "level"
    val W_USER = "user_id"
    val CREATE_TABLE_WORD = (
                    "CREATE TABLE IF NOT EXISTS $WORD (" +
                    "$W_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$W_WORD TEXT DEFAULT \"\", " +
                    "$W_SOUND TEXT DEFAULT \"\", " +
                    "$W_LVL INTEGER DEFAULT 0, " +
                    "$W_DATE DATE NOT NULL, " +
                    "$W_USER INTEGER NOT NULL, " +
                    "FOREIGN KEY ($W_USER) REFERENCES $PASSWORD ($P_ID)," +
                    "PRIMARY KEY ($W_ID))")
    val DELETE_TABLE_WORD = "DROP TABLE IF EXISTS $WORD"

    // Таблица с переводами слов
    val TRANSLATION = "translation"
    val T_ID = BaseColumns._ID
    val T_WID = "word_id"
    val T_TRANSL = "meaning"
    val CREATE_TABLE_TRANSLATION = (
            "CREATE TABLE IF NOT EXISTS $TRANSLATION (" +
                    "$T_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$T_WID TEXT NOT NULL, " +
                    "$T_TRANSL TEXT DEFAULT \"\", " +
                    "FOREIGN KEY ($T_WID) REFERENCES $WORD ($W_ID)," +
                    "PRIMARY KEY ($T_ID))")
    val DELETE_TABLE_TRANSLATION = "DROP TABLE IF EXISTS $TRANSLATION"

    // Таблица с примерами употреблений
    val EXAMPLE = "example"
    val E_ID = BaseColumns._ID
    val E_EX = "example"
    val CREATE_TABLE_EXAMPLE = (
                    "CREATE TABLE IF NOT EXISTS $EXAMPLE ( " +
                    "$E_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$E_EX TEXT NOT NULL," +
                    "PRIMARY KEY ($E_ID))")
    val DELETE_TABLE_EXAMPLE = "DROP TABLE IF EXISTS $EXAMPLE"

    //  Таблица связывающая слова и их употребление
    val WORD_EXAMPLE = "word_example"
    val WE_W_ID = "word_id"
    val WE_E_ID = "example_id"
    val CREATE_TABLE_WORD_EXAMPLE = (
                    "CREATE TABLE IF NOT EXISTS $WORD_EXAMPLE ( " +
                    "$WE_W_ID INTEGER NOT NULL, " +
                    "$WE_E_ID INTEGER NOT NULL, " +
                    "FOREIGN KEY($WE_W_ID) REFERENCES $WORD ($W_ID), " +
                    "FOREIGN KEY($WE_E_ID) REFERENCES $EXAMPLE ($E_ID), " +
                    "UNIQUE($WE_W_ID, $WE_E_ID))")
    val DELETE_TABLE_WORD_EXAMPLE = "DROP TABLE IF EXISTS $WORD_EXAMPLE"

    const val COLUMN_NAME_TITLE = "title"
    const val COLUMN_NAME_SUBTITLE = "subtitle"
}