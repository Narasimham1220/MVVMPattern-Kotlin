package com.example.mvvmkotlin.projectListView.adapter

import android.view.View
import com.example.mvvmkotlin.projectListView.model.Project

interface ProjectItemListener {
    fun onProjectClicked(view : View, project: Project)

    fun onTaskClicked(project: Project)
}