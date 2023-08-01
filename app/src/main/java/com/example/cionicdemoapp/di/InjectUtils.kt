package com.example.cionicdemoapp.di

import android.app.Application
import android.content.Context
import com.example.cionicdemoapp.core_restful_services.RestfulApiService
import com.example.cionicdemoapp.core_restful_services.RestfulApiServiceImpl
import com.example.cionicdemoapp.core_restful_services.services.get_posts.GetPostsService
import com.example.cionicdemoapp.core_restful_services.services.get_posts.GetPostsServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InjectUtils {

    @Provides
    @Singleton
    fun provideRestfulService(): RestfulApiService = RestfulApiServiceImpl.getInstance()

    @Provides
    @Singleton
    fun provideApplicationContext(application: Application): Context =
        application.applicationContext
}

// view models::
@Module
@InstallIn(ViewModelComponent::class)
object InjectViewModel {

    @Provides
    @ViewModelScoped
    fun provideChatService(restfulApiService: RestfulApiService): GetPostsService =
        GetPostsServiceImpl(restfulApiService)

}
