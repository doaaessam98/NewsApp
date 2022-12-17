package com.example.newsapp.ui.onBoarding

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.databinding.ItemCategoryBinding
import com.example.newsapp.models.Category
import com.example.newsapp.models.Country
import com.example.newsapp.ui.home.CategoryTabsAdapter

class CategoryAdapter (private val onclick:(String)->Unit) :
    ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(CategoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)

       holder. binding.root.setOnClickListener {
            onclick.invoke(item.name)
            this.currentList.forEach { category ->
                category.isSelected = category.name.equals(item.name)
                checkIsCategorySelectedOrNot(category,holder)
                notifyDataSetChanged();


            }
        }
    }
    class CategoryViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
              fun bind(item: Category) {
                 binding.category = item
                binding.executePendingBindings()
            }

        }
    }

private fun checkIsCategorySelectedOrNot(item: Category, holder: CategoryAdapter.CategoryViewHolder) {
    if(item.isSelected){
        holder.binding.btnIsSelected.visibility = View.VISIBLE
    } else{
        holder.binding.btnIsSelected.visibility =View.GONE
    }



}


    class CategoryDiffCallback : DiffUtil.ItemCallback<Category>(){

        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.name == newItem.name

        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }
