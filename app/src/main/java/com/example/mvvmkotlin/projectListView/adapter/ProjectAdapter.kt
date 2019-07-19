package com.example.mvvmkotlin.projectListView.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmkotlin.BR.projectListViewModel
import com.example.mvvmkotlin.projectListView.model.Project
import com.example.mvvmkotlin.R
import com.example.mvvmkotlin.databinding.ProjectListItemBinding
import com.example.mvvmkotlin.projectListView.viewModel.ProjectListViewModel

class ProjectAdapter(var projectListViewModel: ProjectListViewModel) : RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

    internal var projectList: ArrayList<Project>? = null

    fun setProjectList(projectList: ArrayList<Project>) {
        if (this.projectList == null) {
            this.projectList = projectList
            notifyItemRangeInserted(0, projectList.size)
        } else {
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return this@ProjectAdapter.projectList!!.size
                }

                override fun getNewListSize(): Int {
                    return projectList.size
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return this@ProjectAdapter.projectList!![oldItemPosition].id == projectList[newItemPosition].id
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val project = projectList[newItemPosition]
                    val old = projectList[oldItemPosition]
                    return project.id == old.id && project.git_url == old.git_url
                }
            })
            this.projectList = projectList
            result.dispatchUpdatesTo(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val binding = DataBindingUtil
            .inflate<ProjectListItemBinding>(
                LayoutInflater.from(parent.context), R.layout.project_list_item,
                parent, false)
        return ProjectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        holder.binding.project = projectList?.get(position)

        val userActionsListener = object : ProjectItemListener {
            override fun onProjectClicked(view : View, project: Project) {
                val checked = (view as CheckBox).isChecked
                projectListViewModel.selectProjectItem(project, checked)
                notifyItemChanged(position)
            }

            override fun onTaskClicked(project: Project) {
                projectListViewModel.openDetailView(project.name)
            }
        }

        holder.binding.listener = userActionsListener
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return if (projectList == null) 0 else projectList!!.size
    }

    class ProjectViewHolder(val binding: ProjectListItemBinding) : RecyclerView.ViewHolder(binding.root)
}