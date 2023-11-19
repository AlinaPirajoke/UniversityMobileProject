package com.example.university.Model.AppDB

import android.provider.BaseColumns

object AppDbNames : BaseColumns {
    // Бд со словами пользователя
    const val DATABASE_NAME = "words_database.db"
    const val DATABASE_VERSION = 5

    // Таблица с пользователями
    const val PASSWORD = "password"
    const val P_ID = BaseColumns._ID
    const val P_PASS = "passwors"
    const val CREATE_TABLE_PASSWORD = (
                    "CREATE TABLE IF NOT EXISTS $PASSWORD ( " +
                    "$P_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$P_PASS TEXT)")
    const val DELETE_TABLE_PASSWORD = "DROP TABLE IF EXISTS $PASSWORD"

    // Таблица со словами
    const val WORD = "word"
    const val W_ID = BaseColumns._ID
    const val W_WORD = "word"
    const val W_SOUND = "sound"
    const val W_DATE = "test_date"
    const val W_LVL = "level"
    const val W_USER = "user_id"
    const val CREATE_TABLE_WORD = (
                    "CREATE TABLE IF NOT EXISTS $WORD (" +
                    "$W_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$W_WORD TEXT NOT NULL, " +
                    "$W_SOUND TEXT DEFAULT \"\", " +
                    "$W_LVL INTEGER DEFAULT 0, " +
                    "$W_DATE DATE NOT NULL, " +
                    "$W_USER INTEGER NOT NULL, " +
                    "FOREIGN KEY ($W_USER) REFERENCES $PASSWORD ($P_ID))")
    const val DELETE_TABLE_WORD = "DROP TABLE IF EXISTS $WORD"

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
                    "FOREIGN KEY ($T_WORD) REFERENCES $WORD ($W_ID))")
    const val DELETE_TABLE_TRANSLATION = "DROP TABLE IF EXISTS $TRANSLATION"

    // Таблица с примерами употреблений
    const val EXAMPLE = "example"
    const val E_ID = BaseColumns._ID
    const val E_EXMPL = "example"
    const val CREATE_TABLE_EXAMPLE = (
                    "CREATE TABLE IF NOT EXISTS $EXAMPLE ( " +
                    "$E_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$E_EXMPL TEXT NOT NULL)")
    const val DELETE_TABLE_EXAMPLE = "DROP TABLE IF EXISTS $EXAMPLE"

    //  Таблица связывающая слова и их употребление
    const val WORD_EXAMPLE = "word_example"
    const val WE_WORD = "word_id"
    const val WE_EXMPL = "example_id"
    const val CREATE_TABLE_WORD_EXAMPLE = (
                    "CREATE TABLE IF NOT EXISTS $WORD_EXAMPLE ( " +
                    "$WE_WORD INTEGER NOT NULL, " +
                    "$WE_EXMPL INTEGER NOT NULL, " +
                    "FOREIGN KEY($WE_WORD) REFERENCES $WORD ($W_ID), " +
                    "FOREIGN KEY($WE_EXMPL) REFERENCES $EXAMPLE ($E_ID), " +
                    "UNIQUE($WE_WORD, $WE_EXMPL))")
    const val DELETE_TABLE_WORD_EXAMPLE = "DROP TABLE IF EXISTS $WORD_EXAMPLE"

    //  Таблица содержащая статистику
    const val STATISTIC = "word_example"
    const val S_WORD = "word_id"
    const val S_RESULT = "result"
    const val S_LIST = "list_number"
    const val S_IS_NEW = "is_new"
    const val CREATE_TABLE_STATISTIC = (
            "CREATE TABLE IF NOT EXISTS $STATISTIC ( " +
                    "$S_WORD INTEGER NOT NULL, " +
                    "$S_RESULT INTEGER NOT NULL, " +
                    "$S_IS_NEW INTEGER NOT NULL," +
                    "$S_LIST INTEGER, " +
                    "FOREIGN KEY($S_WORD) REFERENCES $WORD ($W_ID))")
    const val DELETE_TABLE_STATISTIC = "DROP TABLE IF EXISTS $STATISTIC"

    const val COLUMN_NAME_TITLE = "title"
    const val COLUMN_NAME_SUBTITLE = "subtitle"
}