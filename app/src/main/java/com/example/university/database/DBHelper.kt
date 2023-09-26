package com.example.university.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.university.database.DBNames.CREATE_TABLE_PASSWORD
import com.example.university.database.DBNames.DATABASE_NAME
import com.example.university.database.DBNames.DATABASE_VERSION
import com.example.university.database.DBNames.DELETE_TABLE_PASSWORD


class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_PASSWORD)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL(DELETE_TABLE_PASSWORD)
        onCreate(db)
    }
}