package com.example.newsapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.databinding.SeperatorViewItemBinding

class SeparatorViewHolder (var binding: SeperatorViewItemBinding): RecyclerView.ViewHolder(binding.root){
      fun bind(date: String) {
             binding.separatorDate.text=date
      }


    companion object {
        fun create(parent: ViewGroup): SeparatorViewHolder {
            val separatorView =
                SeperatorViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return SeparatorViewHolder(separatorView)
        }
    }

}
