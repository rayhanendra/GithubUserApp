package com.example.githubuserapp.db

import android.provider.BaseColumns

internal class UserContract {
    internal class UserColumns: BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite_user"
            const val COLUMN_NAME_ID = "_id"
            const val COLUMN_NAME_USERNAME = "username"
            const val COLUMN_NAME_AVATAR_URL = "avatar_url"
            const val COLUMN_NAME_NAME = "name"
            const val COLUMN_NAME_COMPANY = "company"
            const val COLUMN_NAME_LOCATION = "location"
            const val COLUMN_NAME_REPOSITORY = "repository"
            const val COLUMN_NAME_FAVORITE = "isFav"
        }
    }
}