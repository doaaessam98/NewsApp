package com.example.newsapp.ui.favourite

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.databinding.FavListItemBinding
import com.example.newsapp.models.Article

class FavoriteAdapter:ListAdapter<Article, FavoriteAdapter.FavouriteViewHolder>(FavouriteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view = FavListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FavouriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
    class FavouriteViewHolder(val binding: FavListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Article) {
            binding.favouriteArtical = item
            binding.root.setOnClickListener {
                item?.url?.let { url ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    itemView.context.startActivity(intent)
                }
            }
            binding.executePendingBindings()
        }

    }
}



class FavouriteDiffCallback : DiffUtil.ItemCallback<Article>(){

    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.url == newItem.url

    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }
}