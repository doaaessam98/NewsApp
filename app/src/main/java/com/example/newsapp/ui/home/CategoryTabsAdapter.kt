package com.example.newsapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.databinding.CategoryListItemBinding
import com.example.newsapp.models.Category
import com.example.newsapp.utils.onclick


class CategoryTabsAdapter(private var categories: List<Category>, val onCategoryClick:(String)->Unit) : RecyclerView.Adapter<CategoryTabsAdapter.CategoryViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = CategoryListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

       return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
      val item = categories[position]

        checkIsSelectedOrNot(item,holder)

        holder.binding.tvSelectedCountry.text = item.name
        holder.binding.cardItemCategory.onclick{
            onCategoryClick(item.name)
            categories.forEach { category ->
                category.isSelected = category.name.equals(item.name)
                checkIsSelectedOrNot(category,holder)
                //notifyItemChanged(position);
               notifyDataSetChanged();


            }
        }


    }

    private fun checkIsSelectedOrNot(item: Category, holder: CategoryViewHolder) {
        if(item.isSelected){
            holder.binding.selectedTab.visibility = View.VISIBLE
        } else{
            holder.binding.selectedTab.visibility =View.GONE
        }



    }


    override fun getItemCount(): Int {
       return categories.size
    }


   fun submitList(newCategories: List<Category>){
       categories = newCategories
       this.notifyDataSetChanged()
   }

    class CategoryViewHolder(val binding: CategoryListItemBinding) :RecyclerView.ViewHolder(binding.root)


}

