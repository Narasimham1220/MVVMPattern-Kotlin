package com.example.mvvmkotlin.utils

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmkotlin.projectListView.adapter.ProjectAdapter
import com.example.mvvmkotlin.projectListView.model.Project

object CustomBindingAdapter {
    @JvmStatic
    @BindingAdapter("isVisible")
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }

}