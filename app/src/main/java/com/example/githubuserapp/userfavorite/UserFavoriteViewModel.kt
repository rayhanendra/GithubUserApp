package com.example.githubuserapp.userfavorite

import android.content.ContentValues
import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.githubuserapp.data.User
import com.example.githubuserapp.db.UserContract
import com.example.githubuserapp.db.UserHelper

class UserFavoriteViewModel(private val context: Context) : ViewModel() {
    private lateinit var favoriteHelper : UserHelper
    private var favData : User? = null

    fun setFavorite(username: String, avatar: String) {
        favoriteHelper = UserHelper.getInstance(context)
        favoriteHelper.open()
        val values = ContentValues()
        favData
        values.put(UserContract.UserColumns.COLUMN_NAME_USERNAME, username)
        values.put(UserContract.UserColumns.COLUMN_NAME_AVATAR_URL, avatar)

        val result = favoriteHelper.insert(values)
        favData?.id = result.toInt()
    }

    fun deleteFavorite(id: String) {
        favoriteHelper = UserHelper.getInstance(context)
        favoriteHelper.open()
        favoriteHelper.deleteById(id)
    }

    fun showRecyclerView() {
        favoriteHelper = UserHelper.getInstance(context)
        favoriteHelper.open()
    }

    fun closeDatabase() {
        favoriteHelper = UserHelper.getInstance(context)
        favoriteHelper.open()
        favoriteHelper.close()
    }

}