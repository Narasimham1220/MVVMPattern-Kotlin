package com.example.mvvmkotlin.utils

interface TaskCallback<T> {
    fun onComplete(result: T)
    fun onException(t: Throwable?)
}