package com.example.newsapp.utils

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter


//@BindingAdapter("countryImage")
//fun bindCountryImage(imageView: ImageView) {
//    val context = imageView.context
//        imageView.setImageResource()
//        imageView.contentDescription =""
//
//}


@BindingAdapter("setImageView")
fun setImageViewResource(imageView: ImageView, url: String) {
    val context = imageView.context
    Log.e(TAG, "setImageViewResource: $url", )
    if(url!=null) {
        val id = context.resources.getIdentifier(url, "drawable", context.packageName)
        imageView.setImageResource(id)
    }
}





