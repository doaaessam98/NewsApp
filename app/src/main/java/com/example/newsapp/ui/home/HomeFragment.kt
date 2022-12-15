package com.example.newsapp.ui.home

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.databinding.FragmentHomeBinding
import com.example.newsapp.models.Category


class HomeFragment : Fragment() {

  lateinit var binding: FragmentHomeBinding
  lateinit var categoryTabsAdapter: CategoryTabsAdapter
   private var categories :List<String> = listOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
          categories =HomeFragmentArgs.fromBundle(requireArguments()).selectedCategory.toMutableList()

         setUpCategoriesView()
         observeData()
        return binding.root
    }

    private fun observeData() {

    }

    private fun setUpCategoriesView() {
        val categoryList = createCategoryList(categories)
         categoryList[0].isSelected =true
        categoryTabsAdapter = CategoryTabsAdapter(categoryList){
             onCategoryClick(it)
        }
        binding.rvHomeCategory.apply {
            layoutManager  = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter=categoryTabsAdapter

        }


    }

    private fun createCategoryList(categories: List<String>): List<Category> {
        var array:List<Category> = listOf()
        categories.forEach {
             array =  array.plus(Category(it,""))
        }
        return array
    }

    private fun onCategoryClick(selectedCategory: String) {

    }


}