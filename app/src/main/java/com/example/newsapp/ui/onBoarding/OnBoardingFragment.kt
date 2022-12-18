package com.example.newsapp.ui.onBoarding

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.models.Category
import com.example.newsapp.models.Country
import com.example.newsapp.utils.onclick
import com.example.newsapp.utils.readFromAsset
import com.example.newsapp.utils.translationXAnimation
import com.example.newsapp.databinding.FragmentOnBoardingBinding
const val PREFERENCE_NAME = "shard_name"
const val FIRST_TIME = "first_time"
const val FIRST="first"
class OnBoardingFragment : Fragment() {
   lateinit var  binding: FragmentOnBoardingBinding
    lateinit var   sharedPreference  : SharedPreferences
    lateinit var editor: SharedPreferences.Editor
   lateinit var categoryAdapter: CategoryAdapter
   lateinit var  customDropDownAdapter:CustomDropDownAdapter
   lateinit var  selectedCategoryAdapter: SelectedCategoryAdapter
   private var selectedCategoryList = listOf<String>()
    private var categoryList: List<Category>? = emptyList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnBoardingBinding.inflate(inflater)
        binding.lifecycleOwner = this
        initShared()
        isFirstTime()
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
           removeFromCategory(it)
        }
        binding.rvSelectedCategory.apply {
            layoutManager  = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter=selectedCategoryAdapter
            
        }

    }

    private fun removeFromCategory(name: String) {
               categoryList?.forEach{
                   if(it.name==name){
                       it.isSelected=false
                   }
               }
        categoryAdapter.submitList(categoryList)
    }

    private fun setUpNextButton() {
       binding.btnNext.translationXAnimation(100)
        binding.btnNext onclick :: onNextBtnClicked
    }

    private fun onNextBtnClicked() {

        if(selectedCategoryList.size<3)
                {
                    Toast.makeText(requireContext(), R.string.empty_category_message, Toast.LENGTH_SHORT).show()
                  }

              else{
                 navigateToHomeScreen()
             }


    }



    private fun setUpCategoryRecyclerView() {
         categoryList= readFromAsset("Categories.json",requireContext(),Category::class)
        categoryAdapter = CategoryAdapter {
            if(!selectedCategoryList.contains(it)) {
               selectedCategoryList= selectedCategoryList.plus(it)
                selectedCategoryAdapter.submitList(selectedCategoryList)
            }
        }
        binding.rvCategories.apply {
            layoutManager  = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter=categoryAdapter
            categoryAdapter.submitList(categoryList)
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
        saveInShard()
        val  selectedCountryCode :String= (binding.spinner.selectedItem as Country).code
        val categories : Array<String> = selectedCategoryList.toTypedArray()
        val action = OnBoardingFragmentDirections.actionOnBoardingFragmentToHomeFragment(selectedCountryCode,categories)
      findNavController().navigate(action)


    }
     private fun initShared(){
         sharedPreference   = requireActivity().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
         editor = sharedPreference.edit()
     }
    private fun saveInShard() {
        editor.putBoolean(FIRST_TIME,true)
        editor.apply()
    }

    private fun isFirstTime() {
        if(this::sharedPreference.isInitialized) {
            if(sharedPreference.getBoolean(FIRST_TIME, false)) {
                val b=Bundle()
                b.putString(FIRST_TIME, FIRST)
                findNavController().navigate(R.id.action_onBoardingFragment_to_homeFragment,b)
            }
        }
    }
}