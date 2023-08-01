package com.example.cionicdemoapp.presentation.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.cionicdemoapp.Posts_Screen
import com.example.cionicdemoapp.R
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navController: NavController) {
    val scaleValue = remember {
        Animatable(0.0f)
    }

    LaunchedEffect(key1 = true) {
        //animation:
        scaleValue.animateTo(
            targetValue = 2.0f, animationSpec = tween(durationMillis = 500, easing = {
                OvershootInterpolator(0.5f).getInterpolation(it)
            })
        )

        delay(1000)
        navController.popBackStack()
        navController.navigate(Posts_Screen)
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Logo",
            modifier = Modifier.scale(scaleValue.value)
        )
        Text(
            text = "Cionic App", textAlign = TextAlign.Center, style = MaterialTheme.typography.h6
        )
    }

}