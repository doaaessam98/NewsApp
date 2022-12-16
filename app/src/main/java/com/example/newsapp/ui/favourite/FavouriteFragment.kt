package com.example.newsapp.ui.favourite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.databinding.FragmentFavouriteBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
@AndroidEntryPoint
class FavouriteFragment : Fragment() {

    lateinit var binding:FragmentFavouriteBinding
    lateinit var favoriteAdapter: FavoriteAdapter
    val viewModel :FavouriteViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         binding =FragmentFavouriteBinding.inflate(inflater)
         binding.lifecycleOwner = this
         setUpView()
         observeData()
        return binding.root
    }

    private fun observeData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.favourite.collectLatest{asteroids->
                    favoriteAdapter.submitList(asteroids)

                }
            }
        }
    }

    private fun setUpView() {
       favoriteAdapter= FavoriteAdapter()
        binding.rvFavourite.apply {
            layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            adapter=favoriteAdapter
        }
    }


}