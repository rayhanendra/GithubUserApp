package com.example.githubuserapp

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.databinding.FollowersFragmentBinding

class FollowersFragment: Fragment() {

    private lateinit var binding: FollowersFragmentBinding
    private lateinit var adapter: UserListAdapter
    private lateinit var followersViewModel: FollowersViewModel

    companion object {
        private val ARG_USERNAME = "EXTRA_USERNAME"
        fun newInstance(username: String?) =
            FollowersFragment().apply {
                val fragment = FollowersFragment()
                val bundle = Bundle()
                bundle.putString(ARG_USERNAME, username)
                fragment.arguments = bundle
                Log.d("BUNDLE", bundle.toString())
                return fragment
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FollowersFragmentBinding.inflate(inflater, container, false)
        adapter = UserListAdapter()
        adapter.notifyDataSetChanged()

        binding.rvFollowers.layoutManager = LinearLayoutManager(activity)
        binding.rvFollowers.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(ARG_USERNAME)
        Log.d("USERNAMEFOLLEWRS", username.toString())

        followersViewModel = ViewModelProvider(this).get(FollowersViewModel::class.java)

        followersViewModel.setUser(username)

        followersViewModel.getUsers().observe(this, { userItems ->
            if (userItems !=null ) {
                adapter.setData(userItems)
                Log.d( "FOLLOWERS", userItems.toString())
            }
        })
    }
}