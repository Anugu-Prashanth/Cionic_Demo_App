package com.example.cionicdemoapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cionicdemoapp.presentation.splash.SplashScreen
import com.example.cionicdemoapp.presentation.posts.PostsScreen
import com.example.cionicdemoapp.ui.theme.CionicDemoAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CionicDemoAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Navigation()
                }
            }
        }
    }
}


@Composable
fun Navigation(){
    val navHostController = rememberNavController()
    NavHost(navController = navHostController, startDestination = Splash_Screen ){
        composable(route = Splash_Screen){
            SplashScreen(navHostController)
        }
        composable(route = Posts_Screen){
            PostsScreen()
        }
    }
}

const val Splash_Screen  ="splash_screen"
const val Posts_Screen  ="chat_screen"
