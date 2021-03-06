package com.example.githubuserapp.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.R
import com.example.githubuserapp.SettingsActivity
import com.example.githubuserapp.userfavorite.UserFavoriteActivity
import com.example.githubuserapp.adapter.UserListAdapter
import com.example.githubuserapp.databinding.ActivityMainBinding
import com.example.githubuserapp.userdetail.UserDetailActivity

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: UserListAdapter
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UserListAdapter()
        adapter.notifyDataSetChanged()

        showRecyclerView()

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)

        mainViewModel.getUsers().observe(this, { userItems ->
            if (userItems !=null){
                adapter.setData(userItems)
                showLoading(false)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu!!.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = "Masukkan user"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            /*
            Gunakan method ini ketika search selesai atau OK
            */
            override fun onQueryTextSubmit(username: String?): Boolean {
                mainViewModel.setUser(username.toString())
                showLoading(true)
                return true
            }

            /*
            Gunakan method ini untuk merespon tiap perubahan huruf pada searchView
            */
            override fun onQueryTextChange(username: String?): Boolean {
                mainViewModel.setUser(username.toString())
                showLoading(true)
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_change_settings -> {
                val mIntent = Intent(this, SettingsActivity::class.java)
                startActivity(mIntent)
            }

            R.id.favorite -> {
                val mIntent = Intent(this, UserFavoriteActivity::class.java)
                startActivity(mIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showRecyclerView() {
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.adapter = adapter

        showLoading(false)

        adapter.setOnItemClickCallback(object : UserListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: String?) {
                val intent = Intent (this@MainActivity, UserDetailActivity::class.java)
                intent.putExtra("EXTRA_USERNAME", data)
                startActivity(intent)
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
            binding.rvUser.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.rvUser.visibility = View.VISIBLE
        }
    }
}