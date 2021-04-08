package com.example.githubuserapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserapp.databinding.ItemUserBinding

class UserListAdapter : RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {
    private val mData = ArrayList<User>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setData(items: ArrayList<User>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_user, viewGroup, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        return  mData.size
    }

    inner class UserViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemUserBinding.bind(itemView)

        fun bind (user : User){
//            binding.tvName.text = user.name
            binding.tvUsername.text = user.username

            Glide.with(itemView.context)
                .load(user.avatar)
                .into(binding.imgProfile)

            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(user.username.toString())
                Log.d("ini username", user.username.toString())
            }
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: String){
            Log.d("INI ON ITEM CLICKED", data)
        }
    }
}

