package com.example.cionicdemoapp.repository.posts

import android.annotation.SuppressLint
import android.util.Log
import com.example.cionicdemoapp.core_restful_services.GenericErrorCode
import com.example.cionicdemoapp.core_restful_services.Result
import com.example.cionicdemoapp.core_restful_services.Status
import com.example.cionicdemoapp.core_restful_services.services.get_posts.GetPostsService
import com.example.cionicdemoapp.core_restful_services.services.get_posts.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostsRepository @Inject constructor(private val getPostsService: GetPostsService) {
    private val tag = "PostRepository"
    private lateinit var result: Result<List<Post>>

    @SuppressLint("SuspiciousIndentation")
    suspend fun execute(filter: String = ""): Flow<Result<List<Post>>> = flow {
        try {
            emit(Result(Status.LOADING))
            result = getPostsService.getPosts()
            if (result.status == Status.SUCCESS) {
                val data = result.data?.filter { it.title.contains(filter) }
                result = Result(result.status, data)
            }
            Log.d(tag, "$result")
            emit(result)
        } catch (e: Exception) {
            result = Result(Status.ERROR, null, GenericErrorCode.SOMETHING_WENT_WRONG.message)
            Log.d(tag, "$result")
            emit(result)
        }
    }
}