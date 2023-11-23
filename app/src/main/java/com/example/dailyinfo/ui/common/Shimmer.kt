package com.example.dailyinfo.ui.common

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun Modifier.shimmer(
    durationMillis: Int = 1000,
): Modifier = composed {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f),
    )

    val transition = rememberInfiniteTransition(label = "infinite_shimmer")
    val translateAnimationState = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis), repeatMode = RepeatMode.Restart
        ), label = "shimmer"
    )

    then(
        Modifier
            .background(
                Brush.linearGradient(
                    colors = shimmerColors,
                    start = Offset.Zero,
                    end = Offset(
                        x = translateAnimationState.value,
                        y = translateAnimationState.value
                    )
                )
            )
    )
}