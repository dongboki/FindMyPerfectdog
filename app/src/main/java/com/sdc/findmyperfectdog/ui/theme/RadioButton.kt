package com.sdc.findmyperfectdog.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun CustomRadioButton(
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(16.dp)) {
            // 바깥쪽 원 (항상 검정색)
            drawCircle(
                color = Color.Black,
                radius = size.minDimension / 2,
                style = Stroke(width = 3f) // 선 두께 조절
            )
            if (selected) {
                // 안쪽 원 (선택되었을 때만 FCA651)
                drawCircle(
                    color = Color(0xFFFCA651),
                    radius = size.minDimension / 3
                )
            }
        }
    }
}
