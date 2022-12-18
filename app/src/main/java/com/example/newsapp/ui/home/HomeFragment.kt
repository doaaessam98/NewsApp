package com.example.newsapp.ui.home

import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.databinding.FilterBottomSheetBinding
import com.example.newsapp.databinding.FragmentHomeBinding
import com.example.newsapp.models.*
import com.example.newsapp.ui.onBoarding.CustomDropDownAdapter
import com.example.newsapp.ui.onBoarding.NOT_FIRST
import com.example.newsapp.ui.onBoarding.FIRST_TIME
import com.example.newsapp.ui.onBoarding.PREFERENCE_NAME
import com.example.newsapp.utils.Constants
import com.example.newsapp.utils.onclick
import com.example.newsapp.utils.readFromAsset
import com.example.newsapp.utils.toCategoryList
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import java.lang.reflect.Type


const val PREFERENCE_NAME = "home_shard_name"
const val CATEGRORIES = "categories"
const val COUNTRY = "country"
@AndroidEntryPoint
class HomeFragment : Fragment() {
    lateinit var   sharedPreference  : SharedPreferences
    lateinit var editor: SharedPreferences.Editor
  lateinit var binding: FragmentHomeBinding
  lateinit var categoryTabsAdapter: CategoryTabsAdapter
  lateinit var articleAdapter: ArticleAdapter
    lateinit var  customDropDownAdapter:CustomDropDownAdapter
   private val viewModel:HomeViewModel by viewModels()
     var categoryList = listOf<Category>()
    var categories = mutableListOf<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        binding.lifecycleOwner = this

        if(arguments?.getString(FIRST_TIME)!=NOT_FIRST){
            categories = HomeFragmentArgs.fromBundle(requireArguments()).selectedCategory?.toMutableList() ?:mutableListOf()
            viewModel.selectedCategory = categories[0]
            val country = HomeFragmentArgs.fromBundle(requireArguments()).selectedCpuntryCode
            Constants.DEFULT_COUNTRY =country!!
             savedDataInShared(categories, country)
           }else{

              getDataFromArgs()
        }


        setUpArticleRecycleViewState(
            uiState = viewModel.state,
            pagingData = viewModel.articlePagingDataFlow,
            uiActions = viewModel.onCategoryChange,

        )
        setUpCategoriesView(categories,onCategoryChanged = viewModel.onCategoryChange)
        setUpSearchView(uiActions = viewModel.onSearchChange)
        handelTopBarMenuItem()
        return binding.root
    }

    private fun getDataFromArgs() {
        categories =getListFromShared()
                viewModel.selectedCategory = categories[0]
                 Constants.DEFULT_COUNTRY =sharedPreference.getString(CATEGRORIES,"" )!!




    }
    private fun getListFromShared(): MutableList<String> {
        sharedPreference  = requireActivity().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

        var list: MutableList<String>
        val json: String = sharedPreference.getString(CATEGRORIES,"")!!
        val type: Type = object : TypeToken<ArrayList<String?>?>() {}.type
        list = Gson().fromJson<Any>(json, type) as MutableList<String>
        return list

    }
    private fun savedDataInShared(categories: MutableList<String>, country: String) {
        sharedPreference  = requireActivity().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        editor = sharedPreference.edit()
        val convertedData = Gson().toJson(categories)
        editor.putString(CATEGRORIES, convertedData).apply();
        editor.putString(COUNTRY,country)
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
        handelRecycleScroll(headLineAdapter,uiState)

        lifecycleScope.launch {
            pagingData.collectLatest{
                headLineAdapter.submitData(it)
                binding.rvHeadLine.scrollToPosition(0)
            }

        }

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
                val isListEmpty = loadState.refresh is LoadState.Error || (headLineAdapter.itemCount == 0&&loadState.source.refresh is LoadState.NotLoading)
                binding.emptyList.isVisible = isListEmpty
                binding.rvHeadLine.isVisible =loadState.source.refresh is LoadState.NotLoading || loadState.mediator?.refresh is LoadState.NotLoading
                binding. progressBar.isVisible = loadState.mediator?.refresh is LoadState.Loading ||headLineAdapter.itemCount != 0
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


    private fun updateHeadlineListFromInput( category:String, onCategoryChanged: (UiAction) -> Unit) {
              binding.rvHeadLine.scrollToPosition(0)
              onCategoryChanged(UiAction.GetNews(category = category))



    }
    private fun setUpCategoriesView(
        categories: MutableList<String>,
        onCategoryChanged: (UiAction) -> Unit
    ) {
         categoryList = categories.toCategoryList()
           categoryList.forEach {
               if( it.name==viewModel.selectedCategory){
                   it.isSelected=true
                 }
           }
        categoryTabsAdapter = CategoryTabsAdapter(categoryList){
            updateHeadlineListFromInput(it,onCategoryChanged)
            viewModel.selectedCategory = it
        }
             binding.rvHomeCategory.apply {
            layoutManager  = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter=categoryTabsAdapter

        }


    }

    private fun handelTopBarMenuItem(){

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.favorite -> {
                    findNavController().navigate(R.id.action_homeFragment_to_favouriteFragment)

                    true
                }
                R.id.search -> {
                    binding.topAppBar.visibility=View.GONE
                    binding.searchViewLayout.visibility=View.VISIBLE
                    true
                }
                else -> false
            }
        }
    }


    private fun setUpSearchView(uiActions: (UiAction) -> Unit) {
        setUpFilterButton(uiActions)
        setUpBackButtonInSearch()
        binding.searchNews.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(count == 0) {
                    //searchAdapter.submitList()
                } else {
                    val query = binding.searchNews.text.toString()
                    updateHeadlineListFromSearch(query){
                       uiActions.invoke(it)
                    }
                }
            }
        })

        binding.searchImage.setOnClickListener {
            val query = binding.searchNews.text.toString()
            updateHeadlineListFromSearch(query){
                uiActions.invoke(it)
            }

        }
    }
    private fun updateHeadlineListFromSearch(query: String, searchData: (UiAction.Search) -> Unit) {
        binding.rvHeadLine.scrollToPosition(0)
        val searchQuery = "%$query%"
           searchData(UiAction.Search(query=searchQuery))



    }


    private fun setUpFilterButton(onCategoryChanged: (UiAction) -> Unit) {
        binding.btnFilter.onclick {
            showFilterBottomSheet(onCategoryChanged)
        }
    }
    private fun setUpBackButtonInSearch() {
        binding.btnBack.onclick {
            binding.searchNews.text.clear()
            binding.topAppBar.visibility=View.VISIBLE
            binding.searchViewLayout.visibility=View.GONE

        }
    }

    private fun showFilterBottomSheet(onCategoryChanged: (UiAction) -> Unit) {
        val bottomSheet = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val view: View = layoutInflater.inflate(R.layout.filter_bottom_sheet, null)
        val bindingBottom = FilterBottomSheetBinding.bind(view)
        bindingBottom.root
        bottomSheet.setContentView(view)
        setUpSpinner(bindingBottom)

        bottomSheet.show()

        var type :String = viewModel.selectedCategory

        bindingBottom.chipGroupCategory.setOnCheckedChangeListener { group, selectedChipId ->
            type = group.findViewById<Chip>(selectedChipId).text.toString()

        }


        bindingBottom.btnApply.setOnClickListener {
            if(binding.searchNews.text.toString().isNotEmpty()){
               updateHeadlineListFromInput(type,onCategoryChanged)
                categoryList = categoryList.plus(Category(type,""))
                categoryTabsAdapter.submitList(categoryList)
            }else{
                Toast.makeText(requireContext(), "please enter text to Search about", Toast.LENGTH_SHORT).show()
            }
            bottomSheet.dismiss()
        }


    }
    private fun setUpSpinner(bindingBottom: FilterBottomSheetBinding) {
        val modelList: List<Country>?= readFromAsset("Countries.json",requireContext(),
            Country::class)
        customDropDownAdapter = CustomDropDownAdapter(modelList)
        bindingBottom.searchSpinnerCountry.apply {
            adapter=customDropDownAdapter

        }
     Constants.DEFULT_COUNTRY= (bindingBottom.searchSpinnerCountry.selectedItem as Country).code
    }
    override fun onStart() {
        super.onStart()
        binding.searchNews.text.clear()
        binding.topAppBar.visibility=View.VISIBLE
        binding.searchViewLayout.visibility=View.GONE

    }





}