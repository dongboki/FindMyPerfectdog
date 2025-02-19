package com.sdc.findmyperfectdog.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ToggleFavoriteIcon() {
    var isLiked by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {
        IconButton(
            onClick = { isLiked = !isLiked },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(20.dp) // 상단 우측에 20.dp 띄움
                .size(36.dp)
                .background(
                    color = Color(0xFFD9D9D9),
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favorite",
                modifier = Modifier.size(22.dp),
                tint = if (isLiked) Color.Red else Color.White
            )
        }
    }
}

