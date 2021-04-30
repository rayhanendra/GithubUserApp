package com.example.githubuserapp.userfavorite

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.githubuserapp.db.UserContract.Companion.AUTHORITY
import com.example.githubuserapp.db.UserContract.UserColumns.Companion.CONTENT_URI
import com.example.githubuserapp.db.UserContract.UserColumns.Companion.TABLE_NAME
import com.example.githubuserapp.db.UserHelper

class UserFavoriteProvider : ContentProvider() {

    companion object {
        private const val FAV = 1
        private const val FAV_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var userHelper: UserHelper
        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, FAV)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", FAV_ID)
        }
    }

    override fun onCreate(): Boolean {
        userHelper = UserHelper.getInstance(context as Context)
        userHelper.open()
        return false
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {
        return when (sUriMatcher.match(uri)){
            FAV -> userHelper.queryAll()
            FAV_ID -> userHelper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (FAV) {
            sUriMatcher.match(uri) -> userHelper.insert(values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        val updated: Int = when (FAV_ID) {
            sUriMatcher.match(uri) -> userHelper.update(uri.lastPathSegment.toString(),values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return updated
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val deleted: Int = when (FAV_ID) {
            sUriMatcher.match(uri) -> userHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }
}