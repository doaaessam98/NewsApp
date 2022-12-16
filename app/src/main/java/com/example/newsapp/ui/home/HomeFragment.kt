package com.example.newsapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
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
  lateinit var articleAdapter: ArticleAdapter
   private val viewModel:HomeViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
         val categories  =HomeFragmentArgs.fromBundle(requireArguments()).selectedCategory.toMutableList()
          viewModel.selectedCategory = categories[0]
          Constants.DEFULT_COUNTRY =HomeFragmentArgs.fromBundle(requireArguments()).selectedCpuntryCode
           setUpArticleRecycleViewState(
            uiState = viewModel.state,
            pagingData = viewModel.articlePagingDataFlow,
            uiActions = viewModel.onCategoryChange
        )
        setUpCategoriesView(articleAdapter,categories,onCategoryChanged = viewModel.onCategoryChange)

        return binding.root
    }




 private fun   setUpArticleRecycleViewState(
        uiState: StateFlow<UiState>,
        pagingData: Flow<PagingData<UiModel>>,
        uiActions: (UiAction) -> Unit
    ) {
         articleAdapter = ArticleAdapter{
            viewModel.addToFavourite(it.url)
        }
        val header = ArticleLoadStateAdapter { articleAdapter.retry() }

        setupRecycleView(articleAdapter,header)

        bindArticleList(
            header = header,
            uiState = uiState,
            headLineAdapter=articleAdapter,
            pagingData = pagingData,
            onScrollChanged = uiActions
        )
    }

    private fun setupRecycleView(
        headLineAdapter: ArticleAdapter,
        header: ArticleLoadStateAdapter
    ) {
        binding.rvHeadLine.apply {
            layoutManager  = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = headLineAdapter.withLoadStateHeaderAndFooter(
                header=header,
                footer = ArticleLoadStateAdapter{headLineAdapter.retry()}
            )
        }
    }

    private fun bindArticleList(
        header:ArticleLoadStateAdapter,
        uiState: StateFlow<UiState>,
        headLineAdapter: ArticleAdapter,
        pagingData: Flow<PagingData<UiModel>>,
        onScrollChanged: (UiAction) -> Unit) {

        binding.retryButton.setOnClickListener { headLineAdapter.retry() }
        binding.rvHeadLine.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy != 0) onScrollChanged(UiAction.Scroll(currentCategory = uiState.value.category))
            }
        })

        handelArticleListState(headLineAdapter,header)
        lifecycleScope.launch {
            pagingData.collectLatest{
                headLineAdapter.submitData(it)
                binding.rvHeadLine.scrollToPosition(0)
            }

        }

        handelRecycleScroll(headLineAdapter,uiState)
    }

    private fun handelRecycleScroll(headLineAdapter: ArticleAdapter, uiState: StateFlow<UiState>) {
        val notLoading = headLineAdapter.loadStateFlow
            .asRemotePresentationState().map {
                it == RemotePresentationState.PRESENTED }


        val hasNotScrolledForCurrentSearch = uiState
            .map { it.hasNotScrolledForCurrentCategory }
            .distinctUntilChanged()

        val shouldScrollToTop = combine(
            notLoading,
            hasNotScrolledForCurrentSearch,
            Boolean::and
        ).distinctUntilChanged()



        lifecycleScope.launch {
            shouldScrollToTop.collect { shouldScroll ->
                if (shouldScroll) binding.rvHeadLine.scrollToPosition(0)
            }
        }
    }

    private fun handelArticleListState(
        headLineAdapter: ArticleAdapter,
        header: ArticleLoadStateAdapter
    ) {
        lifecycleScope.launch {
                headLineAdapter.loadStateFlow.collect { loadState ->
                header.loadState = loadState.mediator?.refresh?.takeIf {
                    it is LoadState.Error && headLineAdapter.itemCount > 0 } ?: loadState.prepend
                val isListEmpty = loadState.refresh is LoadState.Error && headLineAdapter.itemCount == 0
                binding.emptyList.isVisible = isListEmpty
                binding.rvHeadLine.isVisible =loadState.source.refresh is LoadState.NotLoading || loadState.mediator?.refresh is LoadState.NotLoading
                binding. progressBar.isVisible = loadState.mediator?.refresh is LoadState.Loading
                binding.retryButton.isVisible = loadState.mediator?.refresh is LoadState.Error && headLineAdapter.itemCount == 0

                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    Toast.makeText(
                        requireContext(),
                        "\uD83D\uDE28 Whoops an error happen",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    }


    private fun updateHeadlineListFromInput(adapter: ArticleAdapter, category:String, onCategoryChanged: (UiAction.GetNews) -> Unit) {
              binding.rvHeadLine.scrollToPosition(0)
              onCategoryChanged(UiAction.GetNews(category = category))



    }
    private fun setUpCategoriesView(
        articleAdapter: ArticleAdapter,
        categories: MutableList<String>,
        onCategoryChanged: (UiAction.GetNews) -> Unit
    ) {
        val categoryList = convert_ListOfString_To_ListOfCategory(categories)
//           categoryList.forEach {
//               if( it.name==viewModel.selectedCategory){
//                   it.isSelected=true
//                 }
//           }
        categoryTabsAdapter = CategoryTabsAdapter(categoryList){
            updateHeadlineListFromInput(adapter = articleAdapter,it,onCategoryChanged)
             // viewModel.selectedCategory = it
        }
             binding.rvHomeCategory.apply {
            layoutManager  = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter=categoryTabsAdapter

        }


    }
    private fun convert_ListOfString_To_ListOfCategory(categories: List<String>): List<Category> {
        var categoryList:List<Category> = listOf()
        categories.forEach {
             categoryList =  categoryList.plus(Category(it,""))
        }
        return categoryList
    }





}