package com.example.githubuserapp.userdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserapp.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class DetailViewModel : ViewModel() {

    private val detailUser = MutableLiveData<User>()

    fun setDetail(username: String?) {

        val client = AsyncHttpClient()
            val url = "https://api.github.com/users/${username}"
            client.addHeader("Authorization", "token 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")
            client.addHeader("User-Agent", "request")
            client.get(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?
                ) {
                    try {
                        //parsing json
                        val result = String(responseBody!!)
                        val responseObject = JSONObject(result)

                        val name = responseObject.getString("name")
                        val username =responseObject.getString("login")
                        val avatar = responseObject.getString("avatar_url")
                        val company = responseObject.getString("company")
                        val location = responseObject.getString("location")
                        val followers = responseObject.getInt("followers")
                        val following = responseObject.getInt("following")
                        val repository = responseObject.getInt("public_repos")

                        val user = User()
                        user.name = name
                        user.username = username
                        user.avatar = avatar
                        user.company = company
                        user.location = location
                        user.followers = followers
                        user.following = following
                        user.repository = repository

                        detailUser.postValue(user)

                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.d("Exception", e.message.toString())
                    }

                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
                ) {
                    Log.d("onFailure", error?.message.toString())
                }
            })
    }

    internal fun getDetailUser(): LiveData<User> {
        return detailUser
    }

}