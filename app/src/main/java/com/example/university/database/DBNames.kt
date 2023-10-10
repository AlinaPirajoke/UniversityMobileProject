package com.example.university.database

import android.provider.BaseColumns

object DBNames : BaseColumns {
    var DATABASE_NAME = "words_database.db"
    val DATABASE_VERSION = 2

    val PASSWORD = "password"
    val P_ID = BaseColumns._ID
    val P_PASS = "passwors"
    val CREATE_TABLE_PASSWORD = (
                    "CREATE TABLE IF NOT EXISTS $PASSWORD ( " +
                    "$P_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$P_PASS TEXT )")
    val DELETE_TABLE_PASSWORD = "DROP TABLE IF EXISTS $PASSWORD"

    val WORD = "word"
    val W_ID = BaseColumns._ID
    val W_WORD = "word"
    val W_SOUND = "sound"
    val W_TRNSL = "TRANSLATION"
    val W_DATE = "test_date"
    val W_LVL = "level"
    val W_USER = "user_id"
    val CREATE_TABLE_WORD = (
                    "CREATE TABLE IF NOT EXISTS $WORD (" +
                    "$W_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$W_WORD TEXT DEFAULT \"\", " +
                    "$W_SOUND TEXT DEFAULT \"\", " +
                    "$W_TRNSL TEXT DEFAULT \"\", " +
                    "$W_LVL INTEGER DEFAULT 0, " +
                    "$W_DATE DATE NOT NULL, " +
                    "$W_USER INTEGER NOT NULL, " +
                    "FOREIGN KEY ($W_USER) REFERENCES $PASSWORD ($P_ID))")
    val DELETE_TABLE_WORD = "DROP TABLE IF EXISTS $WORD"

    val EXAMPLE = "example"
    val E_ID = BaseColumns._ID
    val E_EX = "example"
    val CREATE_TABLE_EXAMPLE = (
                    "CREATE TABLE IF NOT EXISTS $EXAMPLE ( " +
                    "$E_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$E_EX TEXT NOT NULL)")
    val DELETE_TABLE_EXAMPLE = "DROP TABLE IF EXISTS $EXAMPLE"

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