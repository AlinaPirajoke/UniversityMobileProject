package com.example.university.database

import android.provider.BaseColumns

object DBNames : BaseColumns {
    var DATABASE_NAME = "words_database.db"
    val DATABASE_VERSION = 1

    val TABLE_PASSWORD = "password"
    val P_ID = BaseColumns._ID
    val P_PASS = "passwors"
    val CREATE_TABLE_PASSWORD = (
            "CREATE TABLE IF NOT EXISTS $TABLE_PASSWORD (" +
                    "$P_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$P_PASS TEXT )")
    val DELETE_TABLE_PASSWORD = "DROP TABLE IF EXISTS $TABLE_PASSWORD"

    const val COLUMN_NAME_TITLE = "title"
    const val COLUMN_NAME_SUBTITLE = "subtitle"
}