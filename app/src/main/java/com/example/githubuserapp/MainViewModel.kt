package com.example.githubuserapp

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainViewModel : ViewModel() {

    private val listUsers = MutableLiveData<ArrayList<User>>()

    fun setUser(username: String) {
        val listItems = ArrayList<User>()

        val client = AsyncHttpClient()
            val url = "https://api.github.com/search/users?q=${username}"
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
                        val items = responseObject.getJSONArray("items")
                        Log.d("ini items", items.toString())

                        for (i in 0 until items.length()) {
                            val item = items.getJSONObject(i)
                            val username = item.getString("login")
                            val avatar = item.getString("avatar_url")
                            val user = User()
                            user.username = username
                            user.avatar = avatar
                            listItems.add(user)
                        }
                        //set data ke adapter
                        listUsers.postValue(listItems)
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

    fun getUsers(): LiveData<ArrayList<User>> {
        return listUsers
    }
}