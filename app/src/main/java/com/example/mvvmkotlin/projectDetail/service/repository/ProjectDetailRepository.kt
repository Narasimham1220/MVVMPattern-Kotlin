package com.example.mvvmkotlin.projectDetail.service.repository


import com.example.mvvmkotlin.core.network.NetworkManager
import com.example.mvvmkotlin.projectDetail.service.ProjectDetailService
import com.example.mvvmkotlin.projectListView.model.Project
import com.example.mvvmkotlin.utils.Result
import com.example.mvvmkotlin.utils.TaskCallback
import com.example.mvvmkotlin.utils.awaitResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProjectDetailRepository {

    val completableJob = Job()
    private val backgroundScope = CoroutineScope(Dispatchers.IO + completableJob)
    private val foregroundScope = CoroutineScope(Dispatchers.Main)

    val HTTPS_API_GITHUB_URL = "https://api.github.com/"

    private val projectDetailService : ProjectDetailService by lazy {
        NetworkManager.baseUrl(HTTPS_API_GITHUB_URL).serviceClass(ProjectDetailService::class.java).build<ProjectDetailService>()
    }

    fun getProjectDetail(userId : String, repoName : String, callback: TaskCallback<Project>){
        backgroundScope.launch {

            when (val result : Result<Project> = projectDetailService.getProjectDetails(userId, repoName).awaitResult()) {

                /**Successful Network Request*/
                is Result.Ok -> foregroundScope.launch { callback.onComplete(result.value) }

                /**Error on the Network Result*/
                is Result.Error -> foregroundScope.launch {callback.onException(result.exception)}

                /**Exception on the Network Result*/
                is Result.Exception -> foregroundScope.launch {callback.onException(result.exception)}
            }
        }
    }
}