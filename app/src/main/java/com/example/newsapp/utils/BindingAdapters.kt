package com.example.newsapp.utils

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.newsapp.R



@BindingAdapter("setImageView")
fun setImageViewResource(imageView: ImageView, url: String) {
    val context = imageView.context
    Log.e(TAG, "setImageViewResource: $url", )
    if(url!=null) {
        val id = context.resources.getIdentifier(url, "drawable", context.packageName)
        imageView.setImageResource(id)
    }
}
@BindingAdapter("setHeadLineImage")
 fun bindingHeadlineImageView(imageView: ImageView, url: String?){


    val context = imageView.context
    if (url!=null) {
            Glide.with(context)
                .load(url)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.placeholder_image)
                .centerCrop()
                .into(imageView)

        }
 }

@BindingAdapter("setDateFormat")
fun bindAArticleDate(textView: TextView,date:String){
    textView.text= dateFormat(date)
}



