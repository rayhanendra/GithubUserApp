package com.example.githubuserapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.githubuserapp.databinding.ActivityUserDetailBinding

class UserDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var detailViewModel : DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("EXTRA_USERNAME")
        val bundle = Bundle()
        bundle.putString("EXTRA_USERNAME", username)
        Log.d("ini user", username.toString())

        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)
        detailViewModel = DetailViewModel()
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
    }
}