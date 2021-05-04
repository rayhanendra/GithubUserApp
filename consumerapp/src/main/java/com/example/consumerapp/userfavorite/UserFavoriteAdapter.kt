package com.example.consumerapp.userfavorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.consumerapp.R
import com.example.consumerapp.data.UserFavorite
import com.example.consumerapp.databinding.ItemUserBinding

class UserFavoriteAdapter : RecyclerView.Adapter<UserFavoriteAdapter.UserViewHolder>() {

    var listUser = ArrayList<UserFavorite>()
        set(listUser){
            if (listUser.size > 0){
                this.listUser.clear()
            }
            this.listUser.addAll(listUser)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_user, viewGroup, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = this.listUser.size

    inner class UserViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemUserBinding.bind(itemView)

        fun bind (user: UserFavorite){
            binding.tvUsername.text = user.username

            Glide.with(itemView.context)
                    .load(user.avatar)
                    .into(binding.imgProfile)
        }
    }
}