package com.example.university.model.appDB

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.university.model.appDB.AppDbNames.CREATE_TABLE_EXAMPLE
import com.example.university.model.appDB.AppDbNames.CREATE_TABLE_LIBRARY
import com.example.university.model.appDB.AppDbNames.CREATE_TABLE_LIBRARY_USER
import com.example.university.model.appDB.AppDbNames.CREATE_TABLE_LIST
import com.example.university.model.appDB.AppDbNames.CREATE_TABLE_LIST_DATA
import com.example.university.model.appDB.AppDbNames.CREATE_TABLE_LI_TRANSLATION
import com.example.university.model.appDB.AppDbNames.CREATE_TABLE_PASSWORD
import com.example.university.model.appDB.AppDbNames.CREATE_TABLE_TRANSLATION
import com.example.university.model.appDB.AppDbNames.CREATE_TABLE_WORD
import com.example.university.model.appDB.AppDbNames.CREATE_TABLE_WORD_EXAMPLE
import com.example.university.model.appDB.AppDbNames.DATABASE_NAME
import com.example.university.model.appDB.AppDbNames.DATABASE_VERSION
import com.example.university.model.appDB.AppDbNames.DELETE_TABLE_EXAMPLE
import com.example.university.model.appDB.AppDbNames.DELETE_TABLE_LIBRARY
import com.example.university.model.appDB.AppDbNames.DELETE_TABLE_LIBRARY_USER
import com.example.university.model.appDB.AppDbNames.DELETE_TABLE_LIST
import com.example.university.model.appDB.AppDbNames.DELETE_TABLE_LIST_DATA
import com.example.university.model.appDB.AppDbNames.DELETE_TABLE_LI_TRANSLATION
import com.example.university.model.appDB.AppDbNames.DELETE_TABLE_PASSWORD
import com.example.university.model.appDB.AppDbNames.DELETE_TABLE_TRANSLATION
import com.example.university.model.appDB.AppDbNames.DELETE_TABLE_WORD
import com.example.university.model.appDB.AppDbNames.DELETE_TABLE_WORD_EXAMPLE

class AppDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_PASSWORD)
        db?.execSQL(CREATE_TABLE_WORD)
        db?.execSQL(CREATE_TABLE_TRANSLATION)
        db?.execSQL(CREATE_TABLE_EXAMPLE)
        db?.execSQL(CREATE_TABLE_WORD_EXAMPLE)
        db?.execSQL(CREATE_TABLE_LIST)
        db?.execSQL(CREATE_TABLE_LIST_DATA)
        db?.execSQL(CREATE_TABLE_LIBRARY)
        db?.execSQL(CREATE_TABLE_LI_TRANSLATION)
        db?.execSQL(CREATE_TABLE_LIBRARY_USER)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL(DELETE_TABLE_TRANSLATION)
        db?.execSQL(DELETE_TABLE_WORD_EXAMPLE)
        db?.execSQL(DELETE_TABLE_WORD)
        db?.execSQL(DELETE_TABLE_PASSWORD)
        db?.execSQL(DELETE_TABLE_EXAMPLE)
        db?.execSQL(DELETE_TABLE_LIST)
        db?.execSQL(DELETE_TABLE_LIST_DATA)
        db?.execSQL(DELETE_TABLE_LIBRARY)
        db?.execSQL(DELETE_TABLE_LI_TRANSLATION)
        db?.execSQL(DELETE_TABLE_LIBRARY_USER)
        onCreate(db)
    }
}