package com.example.university.model.wordsDB

import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.File
import java.io.FileOutputStream

class WordsDbHelper(val context: Context) :
    SQLiteOpenHelper(context, WordsDbNames.DATABASE_NAME, null, WordsDbNames.DATABASE_VERSION) {
    private val preferences: SharedPreferences = context.getSharedPreferences(
        "${context.packageName}.database_versions",
        Context.MODE_PRIVATE
    )
    private val ASSETS_PATH = "databases"
    private fun installedDatabaseIsOutdated(): Boolean {
        return preferences.getInt(WordsDbNames.DATABASE_NAME, 0) < WordsDbNames.DATABASE_VERSION
    }

    private fun writeDatabaseVersionInPreferences() {
        preferences.edit().apply {
            putInt(WordsDbNames.DATABASE_NAME, WordsDbNames.DATABASE_VERSION)
            apply()
        }
    }

    private fun installDatabaseFromAssets() {
        val inputStream = context.assets.open("$ASSETS_PATH/${WordsDbNames.DATABASE_NAME}")
        try {
            val outputFile = File(context.getDatabasePath(WordsDbNames.DATABASE_NAME).path)
            val outputStream = FileOutputStream(outputFile)
            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.flush()
            outputStream.close()
        } catch (exception: Throwable) {
            throw RuntimeException(
                "The $WordsDbNames.DATABASE_NAME database couldn't be installed.",
                exception
            )
        }
    }

    @Synchronized
    private fun installOrUpdateIfNecessary() {
        if (installedDatabaseIsOutdated()) {
            context.deleteDatabase(WordsDbNames.DATABASE_NAME)
            installDatabaseFromAssets()
            writeDatabaseVersionInPreferences()
        }
    }

    override fun getWritableDatabase(): SQLiteDatabase {
        installOrUpdateIfNecessary()
        return super.getWritableDatabase()
    }

    override fun getReadableDatabase(): SQLiteDatabase {
        installOrUpdateIfNecessary()
        return super.getReadableDatabase()
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Nothing to do
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Nothing to do
    }
}