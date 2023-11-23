package com.example.dailyinfo.ui.common

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun TextNormal11(text: String) {
    Text(
        text = text,
        fontSize = 11.sp,
    )
}

@Composable
fun TextBold11(text: String) {
    Text(
        text = text,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
    )
}


@Composable
fun TextBold14(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun TextNormal12(text: String) {
    Text(
        text = text,
        fontSize = 12.sp,
    )
}