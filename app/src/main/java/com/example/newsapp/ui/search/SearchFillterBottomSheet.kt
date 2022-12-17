package com.example.newsapp.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.R
import com.example.newsapp.databinding.FilterBottomSheetBinding
import com.example.newsapp.models.Country
import com.example.newsapp.ui.onBoarding.CustomDropDownAdapter
import com.example.newsapp.utils.Constants
import com.example.newsapp.utils.readFromAsset

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip

class SearchFillterBottomSheet : BottomSheetDialogFragment() {
    lateinit var  customDropDownAdapter:CustomDropDownAdapter
    private lateinit var binding: FilterBottomSheetBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FilterBottomSheetBinding.inflate(inflater)
        setUpSpinner()

        var chipCategory: String
        binding.chipGroupCategory.setOnCheckedChangeListener { group, selectedChipId ->
            chipCategory = group.findViewById<Chip>(selectedChipId).text.toString()
        }

        Constants.DEFULT_COUNTRY = (binding.searchSpinnerCountry.selectedItem as Country).code

        return binding.root
    }

    private fun setUpSpinner() {
        val modelList: List<Country>?= readFromAsset("Countries.json",requireContext(),Country::class)
        customDropDownAdapter = CustomDropDownAdapter( modelList)
        binding.searchSpinnerCountry.apply {
            adapter=customDropDownAdapter

        }
    }


}