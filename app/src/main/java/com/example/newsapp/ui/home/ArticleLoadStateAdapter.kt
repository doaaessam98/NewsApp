package com.example.newsapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.databinding.ArticleLoadStateFooterBinding

class ArticleLoadStateAdapter(private val retry: () -> Unit):LoadStateAdapter<ArticleLoadStateAdapter.ArticleLoadStateViewHolder>() {

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
         }
         holder.bind(loadState,retry)
     }


     class ArticleLoadStateViewHolder(var binding: ArticleLoadStateFooterBinding)
        :RecyclerView.ViewHolder(binding.root){
             fun bind(loadState: LoadState, retry: () -> Unit){
                 binding.progressBar.isVisible = loadState is LoadState.Loading
                 binding.retryButton.isVisible = loadState is LoadState.Error
                 binding.errorMsg.isVisible = loadState is LoadState.Error
                 binding.retryButton.setOnClickListener { retry.invoke() }
             }
        }
}