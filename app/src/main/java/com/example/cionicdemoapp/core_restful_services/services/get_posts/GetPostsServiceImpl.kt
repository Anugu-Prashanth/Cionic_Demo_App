package com.example.cionicdemoapp.core_restful_services.services.get_posts

import com.example.cionicdemoapp.core_restful_services.GenericErrorCode
import com.example.cionicdemoapp.core_restful_services.RestfulApiService
import com.example.cionicdemoapp.core_restful_services.Result
import com.example.cionicdemoapp.core_restful_services.Status
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

interface GetPostsService {
    suspend fun getPosts(): Result<List<Post>>
}

class GetPostsServiceImpl constructor(private val apiService: RestfulApiService) : GetPostsService {
    private val tag = "GetPostsServiceImpl"
    override suspend fun getPosts(): Result<List<Post>> {
        try {
            val result = apiService.getPosts()
//            Log.d(tag, result.toString())
            if (result.isSuccessful && result.body() != null) {
//                Log.d(tag, result.body().toString())
                if (result.code() == 200) {
                    val id = result.body() ?: emptyList()
                    return Result(status = Status.SUCCESS, data = id)
                }
            } else {
                return Result(Status.ERROR, null, processError(result))
            }
        } catch (e: HttpException) {
            return Result(Status.ERROR, null, GenericErrorCode.SOMETHING_WENT_WRONG.message)
        } catch (e: IOException) {
            return Result(Status.ERROR, null, GenericErrorCode.NO_INTERNET.message)
        }
        return Result(Status.ERROR, null, GenericErrorCode.SOMETHING_WENT_WRONG.message)
    }

    private fun processError(result: Response<List<Post>>): String {
        return when {
            result.code() == 400 -> GenericErrorCode.SOMETHING_WENT_WRONG.message
            result.code() == 404 -> GenericErrorCode.PAGE_NOT_FOUND.message
            else -> GenericErrorCode.SOMETHING_WENT_WRONG.message
        }
    }
}

