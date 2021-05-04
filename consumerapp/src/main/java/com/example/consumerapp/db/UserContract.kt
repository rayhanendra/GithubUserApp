package com.example.consumerapp.db

import android.net.Uri
import android.provider.BaseColumns

internal class UserContract {
    companion object {
        const val AUTHORITY = "com.example.githubuserapp"
        const val SCHEME = "content"
    }

    internal class UserColumns: BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite_user"
            const val COLUMN_NAME_ID = "_id"
            const val COLUMN_NAME_USERNAME = "username"
            const val COLUMN_NAME_AVATAR_URL = "avatar_url"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                    .authority(AUTHORITY)
                    .appendPath(TABLE_NAME)
                    .build()
        }
    }
}