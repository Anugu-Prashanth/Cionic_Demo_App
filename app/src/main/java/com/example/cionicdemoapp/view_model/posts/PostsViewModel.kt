package com.example.cionicdemoapp.view_model.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cionicdemoapp.core_restful_services.Status
import com.example.cionicdemoapp.core_restful_services.services.get_posts.Post
import com.example.cionicdemoapp.di.Sort_A_Z
import com.example.cionicdemoapp.di.Sort_Z_A
import com.example.cionicdemoapp.repository.posts.PostsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(private val repository: PostsRepository) : ViewModel() {
    private val _posts = MutableStateFlow<PostsState>(PostsState.Loading)
    val posts: StateFlow<PostsState> = _posts
    private var _list = emptyList<Post>()

    //for refreshing
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    init {
        getPosts()
    }

    private fun getPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.execute(filter = "optio").collect {
                when (it.status) {
                    Status.SUCCESS -> {
                        _list = it.data ?: emptyList()
                        _posts.value = PostsState.Success(data = _list)
                    }

                    Status.LOADING -> _posts.value = PostsState.Loading
                    Status.ERROR -> _posts.value = PostsState.Error(error = it.message ?: "An unexpected error")
                }
            }
            _isRefreshing.value = false
        }
    }

    fun sort(value: String = "") {
        viewModelScope.launch(Dispatchers.IO) {
            if (_list.isNotEmpty()) {
                var data = _list
                _posts.value = PostsState.Loading
                when (value) {
                    Sort_A_Z -> data = data.sortedBy { it.title }
                    Sort_Z_A -> data = data.sortedByDescending { it.title }
                }
                _posts.value = PostsState.Success(data = data)
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            getPosts()
        }
    }
}


sealed class PostsState {
    data class Success(val data: List<Post>) : PostsState()
    object Loading : PostsState()
    data class Error(val error: String) : PostsState()
}
