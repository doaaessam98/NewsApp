package com.example.newsapp.ui.home

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.databinding.FragmentHomeBinding
import com.example.newsapp.models.Category
import com.example.newsapp.models.UiAction
import com.example.newsapp.models.UiModel
import com.example.newsapp.models.UiState
import com.example.newsapp.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

  lateinit var binding: FragmentHomeBinding
  lateinit var categoryTabsAdapter: CategoryTabsAdapter
   val viewModel:HomeViewModel by viewModels()
   private var categories :List<String> = listOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
          categories =HomeFragmentArgs.fromBundle(requireArguments()).selectedCategory.toMutableList()
          Constants.DEFAULT_CATEGORY = categories[0]
          Constants.DEFULT_COUNTRY =HomeFragmentArgs.fromBundle(requireArguments()).selectedCpuntryCode
           setUpCategoriesView(onCategoryChanged = viewModel.onCategoryChange)
           setUpArticleRecycleViewState(
            uiState = viewModel.state,
            pagingData = viewModel.headlinespagingDataFlow,
            uiActions = viewModel.onCategoryChange
        )
        return binding.root
    }




 private fun   setUpArticleRecycleViewState(
        uiState: StateFlow<UiState>,
        pagingData: Flow<PagingData<UiModel>>,
        uiActions: (UiAction) -> Unit
    ) {
        val   headLineAdapter = HeadLineAdapter({})
        val header = HeadLineLoadStateAdapter { headLineAdapter.retry() }

        binding.rvHeadLine.apply {
            layoutManager  = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = headLineAdapter.withLoadStateHeaderAndFooter(
                header=header,
                footer = HeadLineLoadStateAdapter{headLineAdapter.retry()}
            )

        }

        bindHeadLineList(
            header = header,
            uiState = uiState,
            headLineAdapter=headLineAdapter,
            pagingData = pagingData,
            onScrollChanged = uiActions
        )
    }

    private fun bindHeadLineList(
          header:HeadLineLoadStateAdapter,
        uiState: StateFlow<UiState>,
        headLineAdapter: HeadLineAdapter,
        pagingData: Flow<PagingData<UiModel>>,
        onScrollChanged: (UiAction) -> Unit) {

        //retryButton.setOnClickListener { headLineAdapter.retry() }
        binding.rvHeadLine.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy != 0) onScrollChanged(UiAction.Scroll(currentCategory = uiState.value.category))
            }
        })

//        val notLoading = headLineAdapter.loadStateFlow
//            .asRemotePresentationState().map {
//                it == RemotePresentationState.PRESENTED }


        val hasNotScrolledForCurrentSearch = uiState
            .map { it.hasNotScrolledForCurrentCategory }
            .distinctUntilChanged()

//        val shouldScrollToTop = combine(
//            notLoading,
//            hasNotScrolledForCurrentSearch,
//            Boolean::and
//        ).distinctUntilChanged()

        lifecycleScope.launch {
            pagingData.collectLatest{
                headLineAdapter.submitData(it)
            }

        }

//        lifecycleScope.launch {
//            shouldScrollToTop.collect { shouldScroll ->
//                if (shouldScroll) binding.rvHeadLine.scrollToPosition(0)
//            }
//        }

    }



    private fun updateHeadlineListFromInput(category:String,onCategoryChanged: (UiAction.GetNews) -> Unit) {
              binding.rvHeadLine.scrollToPosition(0)
              onCategoryChanged(UiAction.GetNews(category = category))


    }



    private fun setUpCategoriesView(onCategoryChanged: (UiAction.GetNews) -> Unit) {
        val categoryList = createCategoryList(categories)
         categoryList[0].isSelected =true
        categoryTabsAdapter = CategoryTabsAdapter(categoryList){
            updateHeadlineListFromInput(it,onCategoryChanged)
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





}