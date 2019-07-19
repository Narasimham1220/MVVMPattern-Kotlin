package com.example.mvvmkotlin.projectListView.viewModel

import androidx.lifecycle.*
import com.example.mvvmkotlin.BaseViewModel
import com.example.mvvmkotlin.Event
import com.example.mvvmkotlin.projectListView.model.Project
import com.example.mvvmkotlin.projectListView.service.repository.ProjectRepository
import com.example.mvvmkotlin.utils.SingleLiveEvent
import com.example.mvvmkotlin.utils.TaskCallback

class ProjectListViewModel() : BaseViewModel(){

    private val projectRepository by lazy {
        ProjectRepository()
    }

    private var selectedProjectList : ArrayList<Project> = arrayListOf()

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean>
        get() = _dataLoading

    private val _projectList = MutableLiveData<ArrayList<Project>>().apply { value = arrayListOf() }
    val projectList: LiveData<ArrayList<Project>>
        get() = _projectList

    private val _openNewEvent = MutableLiveData<Event<String>>()

    val openNewEvent: LiveData<Event<String>>
        get() = _openNewEvent

    internal val toastMessage = SingleLiveEvent<String>()

    fun start(){
        _dataLoading.value = true
        projectRepository.getProjectList("amazon", object : TaskCallback<ArrayList<Project>> {
            override fun onComplete(result: ArrayList<Project>) {
                _dataLoading.value = false
                if(result.size > 0){
                    _projectList.value = result
                } else {
                    toastMessage.value = "Sorry, No Data Available"
                }
            }

            override fun onException(t: Throwable?) {
                _dataLoading.value = false
                toastMessage.value = t?.localizedMessage
            }
        })
    }

    fun selectProjectItem(project: Project, isAdded: Boolean) {
        if (isAdded) {
            _projectList.value?.get(_projectList.value?.indexOf(project)?:-1)?.selected = true
            selectedProjectList.add(project)
        } else {
            _projectList.value?.get(_projectList.value?.indexOf(project)?:-1)?.selected = false
            selectedProjectList.remove(project)
        }
    }

    fun openDetailView(repoName : String){
        _openNewEvent.value = Event(repoName)
    }

    override fun onCleared() {
        super.onCleared()
        projectRepository.completableJob.cancel()
    }
}