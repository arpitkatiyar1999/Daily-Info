package com.example.dailyinfo.ui.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
private fun SpacerGeneric(height: Dp = 0.dp, width: Dp = 0.dp) {
    Spacer(
        modifier = Modifier
            .height(height)
            .width(width)
    )
}


@Composable
fun SpacerWidth12() {
    SpacerGeneric(width = 12.dp)
}

@Composable
fun SpacerWidth4() {
    SpacerGeneric(width = 4.dp)
}


@Composable
fun SpacerHeight8() {
    SpacerGeneric(height = 8.dp)
}

@Composable
fun SpacerHeight4() {
    SpacerGeneric(height = 4.dp)
}