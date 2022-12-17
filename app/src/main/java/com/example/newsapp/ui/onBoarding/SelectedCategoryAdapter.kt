package com.example.newsapp.ui.onBoarding

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.databinding.ItemCategoryBinding
import com.example.newsapp.databinding.ItemSelectedCategoryBinding

class SelectedCategoryAdapter( private val onclick:(String)->Unit)
    : ListAdapter<String,SelectedCategoryAdapter.SelectedCategoryViewHolder>(SelectedCategoryDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedCategoryViewHolder {
        val view = ItemSelectedCategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SelectedCategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectedCategoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onclick)
    }

    class SelectedCategoryViewHolder(val binding: ItemSelectedCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String, onclick: (String) -> Unit) {
            binding.tvSelectedCountry.text = item
            binding.removeCategory.setOnClickListener {
                onclick.invoke(item)
            }
            binding.executePendingBindings()
        }

    }
}

    class SelectedCategoryDiffCallback : DiffUtil.ItemCallback<String>() {

        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem==newItem

        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem==newItem
        }

}