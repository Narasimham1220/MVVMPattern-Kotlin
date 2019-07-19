package com.example.mvvmkotlin.projectListView.view

import android.widget.ListView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmkotlin.projectListView.adapter.ProjectAdapter
import com.example.mvvmkotlin.projectListView.model.Project

object ProjectListBindings {
    @BindingAdapter("items")
    @JvmStatic fun setItems(recyclerView: RecyclerView, items: ArrayList<Project>) {
        with(recyclerView.adapter as ProjectAdapter) {
            setProjectList(items)
            notifyDataSetChanged()
        }
    }
}