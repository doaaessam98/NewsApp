package com.example.newsapp.ui.home

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.databinding.SeperatorViewItemBinding
import com.example.newsapp.utils.dateFormat

class SeparatorViewHolder (var binding: SeperatorViewItemBinding): RecyclerView.ViewHolder(binding.root){
      fun bind(date: String) {
          Log.e(TAG, "bind:dateformate1 ${date}", )

          binding.separatorDate.text= dateFormat(date)
          Log.e(TAG, "bind:dateformate ${dateFormat(date)}", )
      }


    companion object {
        fun create(parent: ViewGroup): SeparatorViewHolder {
            val separatorView =
                SeperatorViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return SeparatorViewHolder(separatorView)
        }
    }

}
