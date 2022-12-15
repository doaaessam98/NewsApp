package com.example.newsapp.ui.onBoarding

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentOnBoardingBinding
import com.example.newsapp.models.Category
import com.example.newsapp.models.Country
import com.example.newsapp.utils.onclick
import com.example.newsapp.utils.readFromAsset
import com.example.newsapp.utils.translationXAnimation
import com.example.newsapp.utils.translationYAnimation

class OnBoardingFragment : Fragment() {
   lateinit var  binding: FragmentOnBoardingBinding
   lateinit var categoryAdapter: CategoryAdapter
   lateinit var  customDropDownAdapter:CustomDropDownAdapter
   lateinit var  selectedCategoryAdapter: SelectedCategoryAdapter
   private var selectedCategoryList = listOf<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnBoardingBinding.inflate(inflater)
           setUpView()
        return binding.root
    }

    private fun setUpView() {
        setUpNextButton()
        setUpCountrySpinnerView()
        setUpCategoryRecyclerView()
        setUpSelectedCategoryRecyclerView()

    }

    private fun setUpSelectedCategoryRecyclerView() {
        selectedCategoryAdapter = SelectedCategoryAdapter {
            selectedCategoryList= selectedCategoryList.minus(it)
            selectedCategoryAdapter.submitList(selectedCategoryList)
        }
        binding.rvSelectedCategory.apply {
            layoutManager  = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter=selectedCategoryAdapter
            
        }

    }

    private fun setUpNextButton() {
       binding.btnNext.translationXAnimation(100)
        binding.btnNext onclick :: onNextBtnClicked
    }

    private fun onNextBtnClicked() {

        when{
                selectedCategoryList.isEmpty()->{
                    Toast.makeText(requireContext(), R.string.empty_category_message, Toast.LENGTH_SHORT).show()
                  }

              else->{
                 navigateToHomeScreen()
             }

    }
    }



    private fun setUpCategoryRecyclerView() {
        val modelList: List<Category>?= readFromAsset("Categories.json",requireContext(),Category::class)
        categoryAdapter = CategoryAdapter {
            if(!selectedCategoryList.contains(it)) {
               selectedCategoryList= selectedCategoryList.plus(it)
                selectedCategoryAdapter.submitList(selectedCategoryList)
            }
        }
        binding.rvCategories.apply {
            layoutManager  = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter=categoryAdapter
            categoryAdapter.submitList(modelList)
        }
    }

    private fun setUpCountrySpinnerView() {
        val modelList: List<Country>?= readFromAsset("Countries.json",requireContext(),Country::class)
         customDropDownAdapter = CustomDropDownAdapter( modelList)
        binding.spinner.apply {
            adapter=customDropDownAdapter

        }
    }


    private fun navigateToHomeScreen() {
        val  selectedCountryCode :String = (binding.spinner.selectedItem as Country).code
        val categories : Array<String> = selectedCategoryList.toTypedArray()
        val action = OnBoardingFragmentDirections.actionOnBoardingFragmentToHomeFragment(selectedCountryCode,categories)
      findNavController().navigate(action)



    }


}