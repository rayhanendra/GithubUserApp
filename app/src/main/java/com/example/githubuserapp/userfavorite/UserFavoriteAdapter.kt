package com.example.githubuserapp.userfavorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserapp.R
import com.example.githubuserapp.data.UserFavorite
import com.example.githubuserapp.databinding.ItemUserBinding
import com.example.githubuserapp.userdetail.UserDetailActivity
import org.jetbrains.anko.startActivity

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
        holder.itemView.setOnClickListener{
            it.context.startActivity<UserDetailActivity>("EXTRA_USERNAME" to listUser[position].username)
        }
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