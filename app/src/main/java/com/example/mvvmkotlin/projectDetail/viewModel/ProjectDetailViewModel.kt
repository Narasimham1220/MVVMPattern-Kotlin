package com.example.mvvmkotlin.projectDetail.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmkotlin.BaseViewModel
import com.example.mvvmkotlin.projectDetail.service.repository.ProjectDetailRepository
import com.example.mvvmkotlin.projectListView.model.Project
import com.example.mvvmkotlin.utils.SingleLiveEvent
import com.example.mvvmkotlin.utils.TaskCallback

class ProjectDetailViewModel : BaseViewModel(){

    private val projectDetailRepository by lazy {
        ProjectDetailRepository()
    }

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean>
        get() = _dataLoading

    private val _projectDetail = MutableLiveData<Project>()
    val projectDetail : LiveData<Project>
            get() = _projectDetail

    internal val toastMessage = SingleLiveEvent<String>()

    fun start(repoName : String){
        _dataLoading.value = true
        ProjectDetailRepository().getProjectDetail("amazon", repoName, object : TaskCallback<Project>{
            override fun onComplete(result: Project) {
                _dataLoading.value = false
                _projectDetail.value = result
            }

            override fun onException(t: Throwable?) {
                _dataLoading.value = false
                toastMessage.value = "Sorry, No Data Available"
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        projectDetailRepository.completableJob.cancel()
    }
}