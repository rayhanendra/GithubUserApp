package com.example.githubuserapp.userdetail.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.adapter.UserListAdapter
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

        showLoading(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(ARG_USERNAME)

        followersViewModel = ViewModelProvider(this).get(FollowersViewModel::class.java)

        followersViewModel.setUser(username)

        followersViewModel.getUsers().observe(this, { userItems ->
            showLoading(false)
            if (userItems != null) {
                adapter.setData(userItems)
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}