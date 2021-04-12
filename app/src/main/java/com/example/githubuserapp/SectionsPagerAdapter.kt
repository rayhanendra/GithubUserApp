package com.example.githubuserapp

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(activity: AppCompatActivity, private val username: String?) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        Log.d("username section", username.toString())
        var fragment: Fragment? = null
        when(position) {
            0 -> fragment = FollowersFragment.newInstance(username)
            1 -> fragment = FollowingFragment.newInstance(username)
        }
        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return 2
    }
}