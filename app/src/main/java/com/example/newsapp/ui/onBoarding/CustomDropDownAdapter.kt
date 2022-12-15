package com.example.newsapp.ui.onBoarding



import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder

import com.example.newsapp.databinding.ItemSpinnerCountryBinding
import com.example.newsapp.models.Country



class CustomDropDownAdapter(var dataSource: List<Country>?) : BaseAdapter() {



    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

       val holder:ItemHolder

      if (convertView == null) {
            var view  = ItemSpinnerCountryBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
           holder = ItemHolder(view)
           holder.binding.root.tag = holder

        } else {
            holder=convertView.tag as  ItemHolder

        }

        holder.binding.country = dataSource?.get(position)
          return holder.binding.root


    }

    override fun getItem(position: Int): Any? {

        return dataSource?.get(position)

    }

    override fun getCount(): Int {
        return dataSource?.size ?: 0;
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    private class ItemHolder(var  binding:ItemSpinnerCountryBinding):ViewHolder(binding.root) {

    }


}
