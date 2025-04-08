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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun BackIcon(
    navController: NavController,
    rootNavController: NavController,
    step: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "뒤로 가기",
            tint = Color.Black,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(24.dp)
                .clickable {
                    if (step == 1) {
                        // 루트 NavController 사용해서 메인 리스트로 이동
                        rootNavController.navigate("AnonymousPostList_screen") {
                            popUpTo(0)
                            launchSingleTop = true
                        }
                    } else {
                        navController.popBackStack()
                    }
                }
        )

        Text(
            text = "$step/7",
            fontSize = 18.sp,
            color = Color.Black,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}


