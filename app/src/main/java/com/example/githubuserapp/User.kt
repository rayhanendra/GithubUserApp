package com.example.githubuserapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    var username: String? = null,
    var name: String? = null,
    var location: String? = null,
    var repository: Int = 0,
    var company: String? = null,
    var followers: Int = 0,
    var following: Int = 0,
    var avatar: String? = null,
) : Parcelable