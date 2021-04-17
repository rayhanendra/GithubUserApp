package com.example.githubuserapp.userdetail.tabs

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.adapter.UserListAdapter
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

        showLoading(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(ARG_USERNAME)

        followingViewModel = ViewModelProvider(this).get(FollowingViewModel::class.java)

        followingViewModel.setUser(username)

        followingViewModel.getUsers().observe(this, { userItems ->
            showLoading(false)
            if (userItems !=null ) {
                adapter.setData(userItems)
            }
        })
        adapter.setOnItemClickCallback(object : UserListAdapter.OnItemClickCallback {})
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}