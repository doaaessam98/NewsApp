package com.example.newsapp.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.databinding.FilterBottomSheetBinding
import com.example.newsapp.databinding.FragmentSearchBinding
import com.example.newsapp.utils.onclick
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
@AndroidEntryPoint
class SearchFragment : Fragment() {
   lateinit var binding:FragmentSearchBinding
   lateinit var searchAdapter: SearchAdapter
   val searchViewModel :SearchViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =FragmentSearchBinding.inflate(inflater)
        setUpSearchView()
         setUpBackButton()
         setUpFilterButton()
         setUpSearchRecycle()
        observeData()
        return binding.root
    }

    private fun setUpSearchView() {
        binding.searchNews.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(count == 0) {
                } else {
                    getSearchData(s.toString())
                }
            }
        })

        binding.searchImage.setOnClickListener {
            searchViewModel.getSearchData(binding.searchNews.text.toString())

        }
    }

    private fun getSearchData(query: String) {
        val searchQuery = "%$query%"
        searchViewModel.getSearchData(query)
    }
    private fun observeData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                searchViewModel.search.collectLatest{it->
                    searchAdapter.submitList(it)

                }
            }
        }
    }

    private fun setUpSearchRecycle() {
        searchAdapter = SearchAdapter()
        binding.rvSearch.apply {
            layoutManager= LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
            adapter=searchAdapter
        }
    }

    private fun setUpFilterButton() {
        binding.btnFilter.onclick {
            showFilterBottomSheet()
        }
    }

    private fun showFilterBottomSheet() {
        val bottomSheet = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val view: View = layoutInflater.inflate(R.layout.filter_bottom_sheet, null)
        val bindingBottom = FilterBottomSheetBinding.bind(view)
        bindingBottom.root
        bottomSheet.setContentView(view)
        bottomSheet.show()


    }

    private fun setUpBackButton() {
        binding.btnBack.onclick {
            findNavController().popBackStack()
        }
    }


}