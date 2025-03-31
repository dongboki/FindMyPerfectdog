package com.sdc.findmyperfectdog.ui.theme

import androidx.compose.runtime.Composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sdc.findmyperfectdog.PretenderFontFamily

@Composable
fun BackIcon(navController: NavController, step: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center // ✅ 모든 요소를 중앙 정렬
    ) {
        // 🔙 뒤로가기 아이콘 (왼쪽 정렬)
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "뒤로 가기",
            tint = Color.Black, // 아이콘 색상
            modifier = Modifier
                .align(Alignment.CenterStart) // ✅ 왼쪽 정렬
                .size(24.dp) // 아이콘 크기
                .clickable { navController.popBackStack() } // 클릭 시 뒤로 가기
        )

        // 📌 진행 단계 표시 ("1/7", "2/7" 등) - 완전히 중앙 정렬됨
        Text(
            text = "$step/7",
            fontSize = 18.sp,
            color = Color.Black,
            modifier = Modifier.align(Alignment.Center) // ✅ 텍스트 중앙 정렬
        )
    }
}

