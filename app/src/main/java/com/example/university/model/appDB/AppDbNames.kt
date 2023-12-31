package com.example.university.model.appDB

import android.provider.BaseColumns

object AppDbNames : BaseColumns {
    // Бд со словами пользователя
    const val DATABASE_NAME = "words_database.db"
    const val DATABASE_VERSION = 12

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

    //  Таблица содержащая списки слов (пройденных вместе)
    const val LIST = "list"
    const val L_ID = BaseColumns._ID 
    const val L_WORD = "word_id"
    const val L_RESULT = "result"
    const val L_IS_NEW = "is_new"
    const val L_IS_FINISHED = "is_finished"
    const val CREATE_TABLE_LIST = (
            "CREATE TABLE IF NOT EXISTS $LIST ( " +
                    "$L_ID INTEGER NOT NULL, " +
                    "$L_WORD INTEGER NOT NULL, " +
                    "$L_RESULT INTEGER DEFAULT 0, " +
                    "$L_IS_NEW INTEGER NOT NULL," +
                    "$L_IS_FINISHED INTEGER NOT NULL DEFAULT 0," +
                    "PRIMARY KEY($L_ID, $L_WORD)," +
                    "FOREIGN KEY($L_WORD) REFERENCES $WORD ($W_ID))")
    const val DELETE_TABLE_LIST = "DROP TABLE IF EXISTS $LIST"

    const val LIST_DATA = "list_data"
    const val LD_LIST = "list_id"
    const val LD_DATE = "date"
    const val LD_USER = "user_id"
    const val CREATE_TABLE_LIST_DATA = (
            "CREATE TABLE IF NOT EXISTS $LIST_DATA ( " +
                    "$LD_LIST INTEGER NOT NULL, " +
                    "$LD_DATE DATE NOT NULL, " +
                    "$LD_USER INTEGER NOT NULL," +
                    "FOREIGN KEY($LD_LIST) REFERENCES $LIST ($L_ID)," +
                    "FOREIGN KEY($LD_USER) REFERENCES $PASSWORD ($P_ID))")
    const val DELETE_TABLE_LIST_DATA = "DROP TABLE IF EXISTS $LIST_DATA"

    // Таблица со стандартной библиотекой слов
    const val LIBRARY = "library"
    const val LI_WORD_ID = BaseColumns._ID
    const val LI_WORD = "word"
    const val LI_SOUND = "sound"
    const val CREATE_TABLE_LIBRARY = (
            "CREATE TABLE IF NOT EXISTS $LIBRARY (" +
                    "$LI_WORD_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$LI_WORD TEXT NOT NULL, " +
                    "$LI_SOUND TEXT DEFAULT \'\')")
    const val DELETE_TABLE_LIBRARY = "DROP TABLE IF EXISTS $LIBRARY"

    // Таблица с переводами слов библиотеки
    const val LI_TRANSLATION = "library_translation"
    const val LIT_ID = BaseColumns._ID
    const val LIT_WORD = "library_word_id"
    const val LIT_TRANSL = "translation"
    const val CREATE_TABLE_LI_TRANSLATION = (
            "CREATE TABLE IF NOT EXISTS $LI_TRANSLATION (" +
                    "$LIT_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$LIT_WORD TEXT NOT NULL, " +
                    "$LIT_TRANSL TEXT NOT NULL, " +
                    "FOREIGN KEY ($LIT_WORD) REFERENCES $LIBRARY ($LI_WORD_ID))")
    const val DELETE_TABLE_LI_TRANSLATION = "DROP TABLE IF EXISTS $LI_TRANSLATION"

    // Таблица, хранящая слова библиотеки, изученные пользователями
    const val LIBRARY_USER = "library_user"
    const val LIU_WORD_ID = "list_id"
    const val LIU_USER = "user_id"
    const val CREATE_TABLE_LIBRARY_USER = (
            "CREATE TABLE IF NOT EXISTS $LIBRARY_USER ( " +
                    "$LIU_WORD_ID INTEGER NOT NULL, " +
                    "$LIU_USER INTEGER NOT NULL," +
                    "FOREIGN KEY($LIU_WORD_ID) REFERENCES $LIBRARY ($LI_WORD_ID)," +
                    "FOREIGN KEY($LIU_USER) REFERENCES $PASSWORD ($P_ID))")
    const val DELETE_TABLE_LIBRARY_USER = "DROP TABLE IF EXISTS $LIST_DATA"

    const val COLUMN_NAME_TITLE = "title"
    const val COLUMN_NAME_SUBTITLE = "subtitle"
}