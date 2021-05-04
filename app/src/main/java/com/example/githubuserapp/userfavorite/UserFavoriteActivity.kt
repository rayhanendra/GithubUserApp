package com.example.githubuserapp.userfavorite


import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.data.UserFavorite
import com.example.githubuserapp.databinding.ActivityUserFavoriteBinding
import com.example.githubuserapp.db.UserContract.UserColumns.Companion.CONTENT_URI
import com.example.githubuserapp.db.UserHelper
import com.example.githubuserapp.helper.MappingFavoriteHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UserFavoriteActivity : AppCompatActivity() {
    private lateinit var adapter: UserFavoriteAdapter
    private lateinit var binding: ActivityUserFavoriteBinding

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvFavorite.setHasFixedSize(true)
        showRecyclerView()
        loadUserAsync()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadUserAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)
        if (savedInstanceState == null) {
            loadUserAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<UserFavorite>(EXTRA_STATE)
            if (list != null) {
                adapter.listUser = list
            }
        }

        showLoading(true)
        supportActionBar?.elevation = 0f
        supportActionBar?.title = "Favorite"
    }

    private fun loadUserAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            showLoading(true)
            val userHelper = UserHelper.getInstance(applicationContext)
            userHelper.open()
            val deferredFav = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingFavoriteHelper.mapCursorToArrayList(cursor)
            }

            userHelper.close()

            showLoading(false)
            val user = deferredFav.await()
            if (user.size > 0){
                adapter.listUser = user
            } else {
                adapter.listUser = ArrayList()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listUser)
    }

    private fun showRecyclerView() {
        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        adapter = UserFavoriteAdapter()
        adapter.notifyDataSetChanged()
        binding.rvFavorite.adapter = adapter

        showLoading(false)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
            binding.rvFavorite.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.rvFavorite.visibility = View.VISIBLE
        }
    }
}