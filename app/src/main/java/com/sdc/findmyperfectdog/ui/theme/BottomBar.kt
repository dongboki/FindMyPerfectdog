package com.sdc.findmyperfectdog.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sdc.findmyperfectdog.R

@Composable
fun BottomNavBar(
    onHomeClick: () -> Unit,
    onBoneClick: () -> Unit,
    onAddClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.Black),
        horizontalArrangement = Arrangement.SpaceEvenly,
        // 세로 정렬(중앙)
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Home 아이콘
        Image(
            painter = painterResource(id = R.drawable.dogstagram),
            contentDescription = "홈",
            modifier = Modifier
                .size(24.dp)
                .clickable { onHomeClick() },
            contentScale = ContentScale.Fit
        )

        // Bone 아이콘
        Image(
            painter = painterResource(id = R.drawable.homescreen),
            contentDescription = "뼈다귀",
            modifier = Modifier
                .size(24.dp)
                .clickable { onBoneClick() },
            contentScale = ContentScale.Fit
        )

        // Add 아이콘
        Image(
            painter = painterResource(id = R.drawable.picture),
            contentDescription = "추가",
            modifier = Modifier
                .size(24.dp)
                .clickable { onAddClick() },
            contentScale = ContentScale.Fit
        )

        // Profile 아이콘
        Image(
            painter = painterResource(id = R.drawable.mypg),
            contentDescription = "프로필",
            modifier = Modifier
                .size(24.dp)
                .clickable { onProfileClick() },
            contentScale = ContentScale.Fit
        )
    }
}
