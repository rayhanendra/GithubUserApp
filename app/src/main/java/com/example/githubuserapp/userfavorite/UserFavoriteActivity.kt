package com.example.githubuserapp.userfavorite

import android.content.Intent
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.PersistableBundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.R
import com.example.githubuserapp.adapter.UserListAdapter
import com.example.githubuserapp.data.User
import com.example.githubuserapp.databinding.ActivityUserDetailBinding
import com.example.githubuserapp.databinding.ActivityUserFavoriteBinding
import com.example.githubuserapp.db.UserContract.UserColumns.Companion.CONTENT_URI
import com.example.githubuserapp.helper.MappingFavoriteHelper
import com.example.githubuserapp.userdetail.UserDetailActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UserFavoriteActivity : AppCompatActivity() {
    private lateinit var adapter: UserListAdapter
    private lateinit var binding: ActivityUserFavoriteBinding
    private lateinit var favoriteViewModel: UserFavoriteViewModel

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UserListAdapter()
        adapter.notifyDataSetChanged()

        favoriteViewModel = ViewModelProvider(this).get(UserFavoriteViewModel::class.java)

        showRecyclerView()

        adapter.setOnDeleteListener { favorite, position ->
            favoriteViewModel.deleteFavorite(favorite.id.toString())
        }

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadUserAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        binding.rvFavorite.adapter = adapter
        if (savedInstanceState == null) {
            loadUserAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<User>(EXTRA_STATE)
            if (list != null) {
                adapter.listFavorite = list
            }
        }

        supportActionBar?.elevation = 0f
        supportActionBar?.title = "Favorite"
    }

    private fun loadUserAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            favoriteViewModel.showRecyclerView()
            val deferredFav = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingFavoriteHelper.mapCursorToArrayList(cursor)
            }
            favoriteViewModel.closeDatabase()
            val favoriteDef = deferredFav.await()
            if (favoriteDef.size > 0) {
                adapter.listFavorite = favoriteDef
            } else {
                adapter.listFavorite = ArrayList()
                showSnackbarMessage("Tidak ada data saat ini")
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavorite)
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding.rvFavorite, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showRecyclerView() {
        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.adapter = adapter

        showLoading(false)

        adapter.setOnItemClickCallback(object : UserListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: String?) {
                val intent = Intent (this@UserFavoriteActivity, UserDetailActivity::class.java)
                intent.putExtra("EXTRA_USERNAME", data)
                startActivity(intent)
            }
        })
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