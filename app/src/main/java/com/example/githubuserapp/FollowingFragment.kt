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
import com.example.githubuserapp.databinding.FollowingFragmentBinding

class FollowingFragment : Fragment() {

    private lateinit var binding: FollowingFragmentBinding
    private lateinit var adapter: UserListAdapter
    private lateinit var followingViewModel: FollowingViewModel

    companion object {
        private val ARG_USERNAME = "EXTRA_USERNAME"
        fun newInstance(username: String?) =
            FollowingFragment().apply {
                val fragment = FollowingFragment()
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
        binding = FollowingFragmentBinding.inflate(inflater, container, false)
        adapter = UserListAdapter()
        adapter.notifyDataSetChanged()

        binding.rvFollowing.layoutManager = LinearLayoutManager(activity)
        binding.rvFollowing.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(ARG_USERNAME)
        Log.d("USERNAMEFLOWINGS", username.toString())

        followingViewModel = ViewModelProvider(this).get(FollowingViewModel::class.java)

        followingViewModel.setUser(username)

        followingViewModel.getUsers().observe(this, { userItems ->
            if (userItems !=null ) {
                adapter.setData(userItems)
                Log.d( "FOLLOWERS", userItems.toString())
            }
        })
    }
}