package com.example.newsapp.ui.search

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.databinding.FavListItemBinding
import com.example.newsapp.models.Article

class SearchAdapter : ListAdapter<Article, SearchAdapter.SearchViewHolder>(SearchDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = FavListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
    class SearchViewHolder(val binding: FavListItemBinding) : RecyclerView.ViewHolder(binding.root) {
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



class SearchDiffCallback : DiffUtil.ItemCallback<Article>(){

    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.url == newItem.url

    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }
}
