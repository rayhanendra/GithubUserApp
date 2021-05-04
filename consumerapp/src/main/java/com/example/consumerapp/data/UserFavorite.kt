package com.example.consumerapp.data

import android.os.Parcelable

import kotlinx.parcelize.Parcelize

@Parcelize
data class UserFavorite(
    var id: Int? = 0,
    var username: String? = "",
    var avatar: String? = "",
) : Parcelable
