package com.example.githubuserapp.helper

import android.database.Cursor
import com.example.githubuserapp.data.User
import com.example.githubuserapp.data.UserFavorite
import com.example.githubuserapp.db.UserContract

object MappingFavoriteHelper {
    fun mapCursorToArrayList(favoriteCursor: Cursor?): ArrayList<UserFavorite> {
        val favoriteList = ArrayList<UserFavorite>()

        favoriteCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(UserContract.UserColumns.COLUMN_NAME_ID))
                val username = getString(getColumnIndexOrThrow(UserContract.UserColumns.COLUMN_NAME_USERNAME))
                val avatar = getString(getColumnIndexOrThrow(UserContract.UserColumns.COLUMN_NAME_AVATAR_URL))
                favoriteList.add(UserFavorite(id, username, avatar))
            }
        }
        return favoriteList
    }
}