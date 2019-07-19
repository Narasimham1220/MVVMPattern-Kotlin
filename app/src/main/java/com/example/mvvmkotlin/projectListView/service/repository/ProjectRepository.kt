package com.example.mvvmkotlin.projectListView.service.repository


import com.example.mvvmkotlin.core.network.NetworkManager
import com.example.mvvmkotlin.projectListView.model.Project
import com.example.mvvmkotlin.projectListView.service.ProjectListService
import com.example.mvvmkotlin.utils.TaskCallback
import com.example.mvvmkotlin.utils.awaitResult
import com.example.mvvmkotlin.utils.Result
import kotlinx.coroutines.*


class ProjectRepository {
    val completableJob = Job()
    private val backgroundScope = CoroutineScope(Dispatchers.IO + completableJob)
    private val foregroundScope = CoroutineScope(Dispatchers.Main)

    private val GITHUB_URL = "https://api.github.com/"

    private val projectListService : ProjectListService by lazy {
        NetworkManager.baseUrl(GITHUB_URL).serviceClass(ProjectListService::class.java).build<ProjectListService>()
    }

    fun getProjectList(userId: String, callback: TaskCallback<ArrayList<Project>>){
        backgroundScope.launch {

            when (val result : Result<ArrayList<Project>> = projectListService.getProjectList(userId).awaitResult()) {

                /**Successful Network Request*/
                is Result.Ok -> foregroundScope.launch { callback.onComplete(result.value) }

                /**Error on the Network Result*/
                is Result.Error -> foregroundScope.launch { callback.onException(result.exception) }

                /**Exception on the Network Result*/
                is Result.Exception -> foregroundScope.launch { callback.onException(result.exception) }
            }
        }
    }

}