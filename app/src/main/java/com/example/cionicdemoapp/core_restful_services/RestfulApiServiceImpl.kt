package com.example.cionicdemoapp.core_restful_services

import com.example.cionicdemoapp.core_restful_services.RestfulApiService.Companion.PROD_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestfulApiServiceImpl {

    companion object {
        private var retrofitService: RestfulApiService? = null
        fun getInstance(): RestfulApiService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(PROD_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RestfulApiService::class.java)
            }
            return retrofitService!!
        }
    }
}