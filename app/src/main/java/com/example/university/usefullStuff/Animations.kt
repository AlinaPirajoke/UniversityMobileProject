package com.example.university.usefullStuff

import android.util.Log
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.university.R

private const val TAG = "Animation"

@Composable
fun LoadAnimation(){

    Log.i(TAG, "Должна показываться анимация")
    val infiniteTransition = rememberInfiniteTransition()

    val sizeValue by infiniteTransition.animateFloat(
        initialValue = 30.dp.value,
        targetValue = 80.dp.value,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ), label = "Load animation"
    )
//    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colors.background.copy(alpha = 0f)) {
    Column(Modifier.fillMaxSize(1f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Icon(
            modifier = Modifier
                .size(sizeValue.dp),
            imageVector = ImageVector.vectorResource(R.drawable.loading),
            contentDescription = "",
            tint = MaterialTheme.colors.primary,
        )
    }
//    }
}

@Composable
fun DownloadAnimation(){

    Log.i(TAG, "Должна показываться анимация")
    val infiniteTransition = rememberInfiniteTransition()

    val paddingValue by infiniteTransition.animateFloat(
        initialValue = 0.dp.value,
        targetValue = 80.dp.value,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ), label = "download animation"
    )
    Column(Modifier.fillMaxSize(1f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Icon(
            modifier = Modifier
                .padding(top = paddingValue.dp)
                .size(50.dp),
            imageVector = ImageVector.vectorResource(R.drawable.downloading),
            contentDescription = "",
            tint = MaterialTheme.colors.primary,
        )
    }
}