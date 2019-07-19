package com.example.mvvmkotlin.core.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.example.mvvmkotlin.R
import com.example.mvvmkotlin.utils.PicassoCircleTransform
import com.squareup.picasso.Picasso

class SequrImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) : AppCompatImageView(context, attrs, defStyleAttr){

    /**
     * Sets image url.
     *
     * @param url the url
     */
    fun setImageUrl(url: String) {
        setImageUrl(url, R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_foreground)
    }

    /**
     * Sets image url.
     *
     * @param url         the url
     * @param placeHolder the place holder
     * @param errorImage  the error image
     */
    fun setImageUrl(url: String, placeHolder: Int, errorImage: Int) {
        try {
            Picasso.get()
                .load(url)
                .transform(PicassoCircleTransform())
                .fit()
                .centerCrop()
                .placeholder(placeHolder)
                .error(errorImage)
                .into(this)
        } catch (e: Exception) {
            //need to add the LogActivity after merge the existing branch
        }

    }
}