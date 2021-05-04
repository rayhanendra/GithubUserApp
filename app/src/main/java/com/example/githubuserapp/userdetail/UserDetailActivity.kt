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
import com.example.githubuserapp.data.UserFavorite
import com.example.githubuserapp.databinding.ActivityUserDetailBinding
import com.example.githubuserapp.db.UserContract
import com.example.githubuserapp.db.UserContract.UserColumns.Companion.CONTENT_URI
import com.example.githubuserapp.db.UserHelper
import com.example.githubuserapp.helper.MappingFavoriteHelper
import com.example.githubuserapp.userdetail.tabs.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var userDetailViewModel : UserDetailViewModel
    private lateinit var userHelper: UserHelper
    private lateinit var uriWithId: Uri
    private var stateFavorite = false
    private var user = UserFavorite()

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_followers,
            R.string.tab_text_following,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("EXTRA_USERNAME")
        val bundle = Bundle()
        bundle.putString("EXTRA_USERNAME", username)

        userDetailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserDetailViewModel::class.java)
        userDetailViewModel = UserDetailViewModel()
        setUserData(username)

        setTab(username)

        binding.btnFavorite.setOnClickListener {
            addFavorite(stateFavorite)
        }

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

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
            user.id = it.id
            user.username = it.username
            user.avatar = it.avatar
            checkFavorite()
        })
    }

    private fun checkFavorite() {
        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + user.id)
        val cursor = contentResolver.query(uriWithId, null, null, null, null)
        val favoriteMap = MappingFavoriteHelper.mapCursorToArrayList(cursor)

        for (data in favoriteMap) {
            if (user.id == data.id) {
                binding.iconFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
                stateFavorite = true
            }
        }
    }

    private fun addFavorite(favoriteState: Boolean) {
            if(favoriteState) {
                setStateFavorite()
                stateFavorite = false
                userHelper.deleteById(user.id.toString())
            }   else {
                setStateFavorite()
                val values = ContentValues()
                values.put(UserContract.UserColumns.COLUMN_NAME_ID, user.id)
                values.put(UserContract.UserColumns.COLUMN_NAME_USERNAME, user.username)
                values.put(UserContract.UserColumns.COLUMN_NAME_AVATAR_URL, user.avatar)
                stateFavorite = true
                contentResolver.insert(CONTENT_URI, values)
            }
    }

    private fun setStateFavorite() {
            if (stateFavorite) {
                binding.iconFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                Toast.makeText(this, "Removed from favorite", Toast.LENGTH_SHORT).show()
            } else {
                binding.iconFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
                Toast.makeText(this, "Added to favorite ", Toast.LENGTH_SHORT).show()
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