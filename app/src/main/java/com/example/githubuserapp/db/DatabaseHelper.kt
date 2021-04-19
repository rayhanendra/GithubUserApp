package com.example.githubuserapp.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.githubuserapp.db.UserContract.UserColumns.Companion.TABLE_NAME

internal class  DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "dbgithubapp"

        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_NOTE = "CREATE TABLE $TABLE_NAME" +
                " (${UserContract.UserColumns.COLUMN_NAME_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " (${UserContract.UserColumns.COLUMN_NAME_USERNAME} TEXT NOT NULL," +
                " ${UserContract.UserColumns.COLUMN_NAME_AVATAR_URL} TEXT NOT NULL," +
                " ${UserContract.UserColumns.COLUMN_NAME_NAME} TEXT NOT NULL," +
                " ${UserContract.UserColumns.COLUMN_NAME_COMPANY} TEXT NOT NULL," +
                " ${UserContract.UserColumns.COLUMN_NAME_LOCATION} TEXT NOT NULL)" +
                " ${UserContract.UserColumns.COLUMN_NAME_REPOSITORY} TEXT NOT NULL," +
                " ${UserContract.UserColumns.COLUMN_NAME_FAVORITE} TEXT NOT NULL"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_NOTE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}