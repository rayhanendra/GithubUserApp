package com.example.githubuserapp.userdetail

import android.content.ContentValues
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuserapp.R
import com.example.githubuserapp.data.User
import com.example.githubuserapp.databinding.ActivityUserDetailBinding
import com.example.githubuserapp.db.UserContract.UserColumns.Companion.COLUMN_NAME_AVATAR_URL
import com.example.githubuserapp.db.UserContract.UserColumns.Companion.COLUMN_NAME_COMPANY
import com.example.githubuserapp.db.UserContract.UserColumns.Companion.COLUMN_NAME_FAVORITE
import com.example.githubuserapp.db.UserContract.UserColumns.Companion.COLUMN_NAME_LOCATION
import com.example.githubuserapp.db.UserContract.UserColumns.Companion.COLUMN_NAME_NAME
import com.example.githubuserapp.db.UserContract.UserColumns.Companion.COLUMN_NAME_REPOSITORY
import com.example.githubuserapp.db.UserContract.UserColumns.Companion.COLUMN_NAME_USERNAME
import com.example.githubuserapp.db.UserContract.UserColumns.Companion.CONTENT_URI
import com.example.githubuserapp.db.UserHelper
import com.example.githubuserapp.helper.MappingFavoriteHelper
import com.example.githubuserapp.userdetail.tabs.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var userDetailViewModel : UserDetailViewModel
    private var stateFavorite = false
    private var user: User?= null
    private lateinit var uriWithId: Uri
    private var position: Int = 0
    private lateinit var userHelper: UserHelper
    private lateinit var imgProfile: String

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_followers,
            R.string.tab_text_following,
        )
        const val favoriteData = "FAVORITE_DATA"
        const val positionData = "POSITION_DATA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("EXTRA_USERNAME")
        val bundle = Bundle()
        bundle.putString("EXTRA_USERNAME", username)

        userDetailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserDetailViewModel::class.java)
        setUserData(username)

        checkDatabaseFavorite()

        setStateFavorite(stateFavorite)
        binding.btnFavorite.setOnClickListener {
            stateFavorite =! stateFavorite
            setStateFavorite(stateFavorite)
            toggleFavorite()
        }

        setTab(username)

        supportActionBar?.elevation = 0f
    }

    private fun setUserData(username: String?){
        userDetailViewModel.setDetail(username)
        userDetailViewModel.getDetailUser().observe(this, {

            binding.apply {
                tvName.text = it.name
                tvUsername.text = it.username
                Glide.with(this@UserDetailActivity)
                    .load(it.avatar)
                    .into(binding.imgProfile)
                tvFollowers.text = it.followers.toString()
                tvFollowing.text = it.following.toString()
                tvRepository.text = it.repository.toString()
                tvCompany.text = it.company ?: "-"
                tvLocation.text = it.location ?: "-"
            }
        })
    }

    private fun checkDatabaseFavorite() {
        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + user?.id)
        val cursor = contentResolver.query(uriWithId, null, null, null, null)
        val myFavorites = MappingFavoriteHelper.mapCursorToArrayList(cursor)
        for (data in myFavorites) {
            if (user?.id == data.id) {
                stateFavorite = true
                setFavoriteData()
            }
        }
    }

    private fun setFavoriteData() {
        user = intent.getParcelableExtra(favoriteData)

        if (user != null) {
            position = intent.getIntExtra(positionData, 0)

            uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + user?.username)
            val cursor = contentResolver.query(uriWithId, null, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                user = MappingFavoriteHelper.mapCursorToObject(cursor)
                cursor.close()
            }
        } else {
            user = User()
        }
    }


    private fun toggleFavorite(){
        if(stateFavorite){
            userHelper.deleteById(user?.username.toString())
            stateFavorite = false
        } else {
            val username = binding.tvUsername.text.toString()
            val avatar = imgProfile
            val name = binding.tvName.text.toString()
            val company = binding.tvCompany.text.toString()
            val location = binding.tvLocation.text.toString()
            val repository = binding.tvRepository.text.toString()
            val fav = "1"

            val values = ContentValues()
            values.put(COLUMN_NAME_USERNAME, username)
            values.put(COLUMN_NAME_AVATAR_URL, avatar)
            values.put(COLUMN_NAME_NAME, name)
            values.put(COLUMN_NAME_REPOSITORY, repository)
            values.put(COLUMN_NAME_COMPANY, company)
            values.put(COLUMN_NAME_LOCATION, location)
            values.put(COLUMN_NAME_FAVORITE, fav)

            contentResolver.insert(CONTENT_URI, values)
            stateFavorite = true
        }
    }

    private fun setStateFavorite(favoriteState: Boolean) {
        if (favoriteState) {
            binding.iconFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
            Toast.makeText(this, "Added to favorite", Toast.LENGTH_SHORT).show()
            }
        else {
            binding.iconFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            Toast.makeText(this, "Removed from favorite", Toast.LENGTH_SHORT).show()
            }
        }

    private fun setTab(username: String?) {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, username)
        val viewPager: ViewPager2 = binding.viewPager

        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }
}