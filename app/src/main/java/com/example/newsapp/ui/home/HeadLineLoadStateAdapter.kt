package com.example.newsapp.ui.home

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.databinding.ArticleLoadStateFooterBinding

class HeadLineLoadStateAdapter(private val retry: () -> Unit):LoadStateAdapter<HeadLineLoadStateAdapter.ArticleLoadStateViewHolder>() {

     override fun onCreateViewHolder(
         parent: ViewGroup,
         loadState: LoadState
     ): ArticleLoadStateViewHolder {
          val view = ArticleLoadStateFooterBinding.inflate(
              LayoutInflater.from(parent.context),parent,false)
         return ArticleLoadStateViewHolder(view)
     }


     override fun onBindViewHolder(holder: ArticleLoadStateViewHolder, loadState: LoadState) {

         if (loadState is LoadState.Error) {
            holder. binding.errorMsg.text =" something error happen"
             Log.e(TAG, "onBindViewHolder: ${loadState.error.localizedMessage}", )
         }
         holder.binding.progressBar.isVisible = loadState is LoadState.Loading
         holder. binding.retryButton.isVisible = loadState is LoadState.Error
         holder. binding.errorMsg.isVisible = loadState is LoadState.Error

        holder. binding.retryButton.setOnClickListener { retry.invoke() }
     }


     class ArticleLoadStateViewHolder(var binding: ArticleLoadStateFooterBinding)
        :RecyclerView.ViewHolder(binding.root)
}