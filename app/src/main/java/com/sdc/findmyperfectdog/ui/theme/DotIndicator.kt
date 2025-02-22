package com.sdc.findmyperfectdog.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DotsIndicator(
    totalDots: Int,
    selectedIndex: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(totalDots) { index ->
            // 선택된 Dot만 조금 더 크게 표시 (예: 10.dp vs 8.dp)
            val dotSize = if (index == selectedIndex) 8.dp else 6.dp
            Box(
                modifier = Modifier
                    .size(dotSize)
                    .clip(CircleShape)
                    .background(
                        if (index == selectedIndex) Color.Black else Color.LightGray
                    )
            )
        }
    }
}


@Composable
fun LoginDotsIndicator(
    totalDots: Int,
    selectedIndex: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        repeat(totalDots) { index ->
            Box(
                modifier = Modifier
                    .width(if (index == selectedIndex) 32.dp else 8.dp)
                    .height(8.dp)
                    .clip(RoundedCornerShape(100.dp))
                    .background(
                        if (index == selectedIndex) Color(0xFFFFAE00) // 주황색
                        else Color(0xFFD9D9D9) // 회색
                    )
            )
        }
    }
}