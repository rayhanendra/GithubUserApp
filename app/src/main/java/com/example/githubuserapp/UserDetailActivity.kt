package com.example.githubuserapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuserapp.databinding.ActivityUserDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var detailViewModel : DetailViewModel

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
        Log.d("ini user", username.toString())

        val bundle = Bundle()
        bundle.putString("EXTRA_USERNAME", username)

        var fragment = FollowersFragment()
        fragment.arguments = bundle
        Log.d("INI ARGUMENTS", fragment.arguments.toString())



        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)
        detailViewModel.setDetail(username)
        detailViewModel.getDetailUser().observe(this, {

            binding.apply {
                tvName.text = it.name
                tvUsername.text = it.username
                Glide.with(this@UserDetailActivity)
                        .load(it.avatar)
                        .into(binding.imgProfile)
                tvFollowers.text = it.followers.toString()
                tvFollowing.text = it.following.toString()
                tvRepository.text = it.repository.toString()
                tvCompany.text = it.company
                tvLocation.text = it.location
            }
        })


        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager

        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }
}