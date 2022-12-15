package com.example.newsapp.ui.home

import android.os.Bundle
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)

         setUpCategoriesView()
        return binding.root
    }

    private fun setUpCategoriesView() {
        categoryTabsAdapter = CategoryTabsAdapter(listOf(
            Category("entertainment","", isSelected = true),
                    Category("sport",""),
            Category("health","")

        )){
            // onCategoryClick(it)
         }
        binding.rvHomeCategory.apply {
            layoutManager  = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter=categoryTabsAdapter

        }


    }

    private fun onCategoryClick(selectedCategory: String) {

    }


}