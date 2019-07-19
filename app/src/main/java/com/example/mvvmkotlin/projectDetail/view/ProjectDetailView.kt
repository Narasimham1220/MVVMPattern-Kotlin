package com.example.mvvmkotlin.projectDetail.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.mvvmkotlin.BaseFragment
import com.example.mvvmkotlin.MainActivity
import com.example.mvvmkotlin.R
import com.example.mvvmkotlin.databinding.FragmentProjectDetailBinding
import com.example.mvvmkotlin.projectDetail.viewModel.ProjectDetailViewModel
import com.example.mvvmkotlin.utils.obtainViewModel
import com.example.mvvmkotlin.utils.setupActionBar

class ProjectDetailView: BaseFragment(){

    private lateinit var projectDetailBinding : FragmentProjectDetailBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        projectDetailBinding = FragmentProjectDetailBinding.inflate(inflater, container, false).apply {
            projectDetailViewModel = (activity as MainActivity).obtainViewModel(ProjectDetailViewModel::class.java).apply {
                toastMessage.observe(this@ProjectDetailView, Observer {
                        message ->
                    run {
                        if (message != null)
                            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
        return projectDetailBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        projectDetailBinding.lifecycleOwner = this.viewLifecycleOwner
        projectDetailBinding.projectDetailViewModel?.start(arguments?.getString(ARGUMENT_REPO_NAME)!!)
    }

    private fun setUpActionBar() {
        (activity as MainActivity).setupActionBar(R.id.toolbar){
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = "Project Detail"
        }
    }

    override fun getTitle(): String {
        return "Project Detail"
    }

    companion object {

        const val ARGUMENT_REPO_NAME = "REPO_NAME"

        fun newInstance(repoName: String) = ProjectDetailView().apply {
            arguments = Bundle().apply {
                putString(ARGUMENT_REPO_NAME, repoName)
            }
        }
    }


}