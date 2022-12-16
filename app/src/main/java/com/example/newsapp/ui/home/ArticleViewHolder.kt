package com.example.newsapp.ui.home

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.databinding.ArticleListItemBinding
import com.example.newsapp.models.Article
import com.example.newsapp.utils.onclick

class ArticleViewHolder(val binding: ArticleListItemBinding):RecyclerView.ViewHolder(binding.root) {

    fun bind(headLine:Article?,onclick:(Article)->Unit) {
        Log.e(TAG, "bindadapter: ${headLine}", )
        if (headLine == null) {
            val resources = itemView.resources
            binding.headLineImage.setImageResource(R.drawable.placeholder_image)
            binding.title.text = resources.getString(R.string.loading)
            binding. description.visibility = View.GONE
            binding. source.visibility = View.GONE
            binding.btnAddFav.isEnabled = false

        } else {
            showRepoData(headLine,onclick)
        }

    }
    private fun showRepoData(article: Article, onclick: (Article) -> Unit) {
        binding. description.visibility = View.VISIBLE
        binding. source.visibility = View.VISIBLE
        binding.btnAddFav.isEnabled = true
        binding.headline = article
        itemView.onclick {
            article?.url?.let { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                itemView.context.startActivity(intent)
            }
        }

        binding.btnAddFav.onclick {

            onclick(article)
        }

    }

    companion object {
        fun create(parent: ViewGroup): ArticleViewHolder {
            val view =
                ArticleListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ArticleViewHolder(view)
        }
    }





}