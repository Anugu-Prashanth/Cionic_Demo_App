package com.example.cionicdemoapp.core_restful_services

data class Result<T>(
    val status: Status,
    val data: T?=null,
    val message: String? = null
) {
    override fun toString(): String {
        return "Result(status=$status, data=$data, error=$message)"
    }
}

enum class Status{
    SUCCESS,
    ERROR,
    LOADING
}