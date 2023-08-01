package com.example.cionicdemoapp.presentation.posts

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cionicdemoapp.R
import com.example.cionicdemoapp.core_restful_services.services.get_posts.Post
import com.example.cionicdemoapp.di.Sort_A_Z
import com.example.cionicdemoapp.di.Sort_Z_A
import com.example.cionicdemoapp.presentation.CustomDialog
import com.example.cionicdemoapp.view_model.posts.PostsState
import com.example.cionicdemoapp.view_model.posts.PostsViewModel

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PostsScreen() {
    val viewModel = hiltViewModel<PostsViewModel>()
    val state by viewModel.posts.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val pullRefreshState = rememberPullRefreshState(isRefreshing, { viewModel.refresh() })
    val lazyListState = rememberLazyListState()
    var showDialog by remember { mutableStateOf(false) }


    Scaffold(topBar = {
        TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) },
            navigationIcon = {
                IconButton(onClick = {

                }) { Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu") }
            },
            actions = {
                Row {
                    IconButton(onClick = { showDialog = true }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Menu")
                    }
                    val options = listOf(
                        Sort_A_Z, Sort_Z_A
                    )
                    ThreeDotMenuOptions(options) { option ->
                        when (option) {
                            0 -> viewModel.sort(value = Sort_A_Z)
                            1 -> viewModel.sort(value = Sort_Z_A)
                        }
                    }
                }
            })
    }) {
        Box(
            Modifier.pullRefresh(pullRefreshState)
        ) {

            LazyColumn(state = lazyListState) {
                when (state) {
                    PostsState.Loading -> item { MessageProgressBar(modifier = Modifier.fillParentMaxSize()) }
                    is PostsState.Error -> item {
                        MessageProgressBar(
                            (state as PostsState.Error).error,
                            modifier = Modifier.fillParentMaxSize()
                        )
                    }

                    is PostsState.Success -> {
                        itemsIndexed((state as PostsState.Success).data) { index, post ->
                            PostListItemBox(post, index)
                            Divider()
                        }
                    }
                }
            }
            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }

    if (showDialog) {
        AlertDialog(onDismissRequest = { showDialog = false }, title = {
            Text(
                text = "Coming Soon...",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }, confirmButton = {
            TextButton(onClick = { showDialog = false }) {
                Text(text = "Ok")
            }
        })
    }
}

@Composable
fun ThreeDotMenuOptions(options: List<String>, onClick: (Int) -> Unit) {

    val expandedState = remember { mutableStateOf(false) }
    IconButton(onClick = { expandedState.value = true }) {
        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Menu")
    }

    DropdownMenu(expanded = expandedState.value,
        onDismissRequest = { expandedState.value = false }) {
        options.forEachIndexed { index, option ->
            DropdownMenuItem(onClick = {
                expandedState.value = false
                onClick(index)
            }) {
                Text(text = option)
            }
        }
    }

}

@Composable
fun MessageProgressBar(text: String = "Loading...", modifier: Modifier) {
    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = text)
        }

    }
}

@Composable
fun PostListItemBox(post: Post, index: Int) {
    val showDialog = remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .background(getColor(index))
            .fillMaxWidth()
            .padding(8.dp)

    ) {
        Column(modifier = Modifier
            .padding(horizontal = 8.dp)
            .clickable { showDialog.value = true }) {
            Text(text = post.title)
            Text(
                text = post.body, style = MaterialTheme.typography.caption
            )
        }
    }

    if (showDialog.value) {
        CustomDialog(title = post.title,
            subTitle = post.body,
            onNo = { showDialog.value = false }) {
            showDialog.value = false
        }
    }
}

@Composable
private fun getColor(index: Int): Color {
    return if (index % 2 == 0) Color.Blue else Color.Gray
}