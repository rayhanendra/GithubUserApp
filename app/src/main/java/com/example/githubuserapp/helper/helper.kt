package com.example.githubuserapp.helper

import android.database.Cursor
import com.example.githubuserapp.data.User
import com.example.githubuserapp.db.UserContract

object MappingFavoriteHelper {
    fun mapCursorToArrayList(favoritCursor: Cursor?): ArrayList<User> {
        val favoriteList = ArrayList<User>()

        favoritCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(UserContract.UserColumns.COLUMN_NAME_ID))
                val username = getString(getColumnIndexOrThrow(UserContract.UserColumns.COLUMN_NAME_USERNAME))
                val avatar = getString(getColumnIndexOrThrow(UserContract.UserColumns.COLUMN_NAME_AVATAR_URL))
                favoriteList.add(User(id, username, avatar))
            }
        }
        return favoriteList
    }

    fun mapCursorToObject(favoriteCursor: Cursor?): User {
        var userFavorite = User()
        favoriteCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(UserContract.UserColumns.COLUMN_NAME_ID))
            val username = getString(getColumnIndexOrThrow(UserContract.UserColumns.COLUMN_NAME_USERNAME))
            val avatar = getString(getColumnIndexOrThrow(UserContract.UserColumns.COLUMN_NAME_AVATAR_URL))
            userFavorite = User(id, username, avatar)
        }
        return userFavorite
    }
}