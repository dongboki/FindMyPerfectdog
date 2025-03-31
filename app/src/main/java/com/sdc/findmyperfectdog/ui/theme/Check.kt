package com.sdc.findmyperfectdog.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Check(selected: Boolean, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start= 4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Check Icon",
            tint = if (selected) Color(0xFF2EB051) else Color.Black, // 선택 여부에 따라 색상 변경
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text, // ✅ 텍스트 추가
            fontSize = 12.sp,
            color = if (selected) Color.Black else Color.Gray
        )
    }
}
