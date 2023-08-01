package com.example.cionicdemoapp.core_restful_services

import com.example.cionicdemoapp.core_restful_services.services.get_posts.Post
import retrofit2.Response
import retrofit2.http.GET

interface RestfulApiService {


    @GET("/posts")
    suspend fun getPosts() : Response<List<Post>>

    companion object {
        const val PROD_URL: String= "https://jsonplaceholder.typicode.com/"

    }
}