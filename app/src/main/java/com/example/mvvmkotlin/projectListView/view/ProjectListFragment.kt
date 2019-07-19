package com.example.mvvmkotlin.projectListView.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmkotlin.BaseFragment
import com.example.mvvmkotlin.Event
import com.example.mvvmkotlin.MainActivity
import com.example.mvvmkotlin.projectListView.adapter.ProjectAdapter
import com.example.mvvmkotlin.projectListView.viewModel.ProjectListViewModel
import com.example.mvvmkotlin.R
import com.example.mvvmkotlin.utils.obtainViewModel
import com.example.mvvmkotlin.databinding.FragmentProjectListBinding
import com.example.mvvmkotlin.projectDetail.view.ProjectDetailView
import com.example.mvvmkotlin.utils.addFragmentToActivity
import com.example.mvvmkotlin.utils.replaceFragmentInActivity
import com.example.mvvmkotlin.utils.setupActionBar
import java.util.ArrayList

class ProjectListFragment : BaseFragment(){

    private lateinit var projectListBinding: FragmentProjectListBinding
    private lateinit var listAdapter : ProjectAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        projectListBinding = FragmentProjectListBinding.inflate(inflater, container, false).apply {
            projectListViewModel = (activity as MainActivity).obtainViewModel(ProjectListViewModel::class.java).apply{
                openNewEvent.observe(this@ProjectListFragment, Observer<Event<String>> { event ->
                    event.getContentIfNotHandled()?.let {
                        openDetailScreen(it)
                    }
                })

                toastMessage.observe(this@ProjectListFragment, Observer {
                    message ->
                    run {
                        if (message != null)
                            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
        return projectListBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        projectListBinding.lifecycleOwner = this.viewLifecycleOwner
        setupListAdapter()
        projectListBinding.projectListViewModel?.start()
    }


    private fun setupListAdapter() {
        val viewModel = projectListBinding.projectListViewModel
        if (viewModel != null) {
            listAdapter = ProjectAdapter(viewModel)
            projectListBinding.projectList.adapter = listAdapter
        } else {
            Log.w(TAG, "ViewModel not initialized when attempting to set up adapter.")
        }
    }


    private fun openDetailScreen(repoName: String) {
        (activity as MainActivity).addFragmentToActivity(ProjectDetailView.newInstance(repoName), R.id.fragment_container, TAG)
    }


    override fun getTitle(): String {
        return  "Google GitHib Project"
    }

    companion object {
        const val TAG = "ProjectListFragment"
        fun newInstance() = ProjectListFragment()
    }
}
