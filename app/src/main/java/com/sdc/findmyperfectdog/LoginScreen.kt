package com.sdc.findmyperfectdog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sdc.findmyperfectdog.ui.theme.LoginDotsIndicator


val PretenderFontFamily = FontFamily(
    Font(R.font.pretendard_regular,)
    // 다른 굵기가 있다면 여기서 추가
)


@Composable
fun LoginScreen(navController: NavController) {
    var currentIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(200.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_logindog),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = "Find Your Dog",
            fontSize = 32.sp,
            lineHeight = 44.sp,
            letterSpacing = -(0.4).sp,
            fontFamily = PretenderFontFamily
        )
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = "당신의 라이프 스타일에 딱 맞는\n강아지를 추천해드립니다.",
            fontSize = 14.sp,
            modifier = Modifier
            , lineHeight = 22.sp,
            fontFamily = PretenderFontFamily

        )
        Spacer(modifier = Modifier.height(180.dp))
        LoginDotsIndicator(
            totalDots = 3,
            selectedIndex = currentIndex,
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { navController.navigate("home_screen") },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFCA651))
            , modifier = Modifier.fillMaxWidth(),

        ) {
            Text(
                text = "시작하기",
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold,
                    lineHeight = 44.sp,
                fontFamily = PretenderFontFamily
            )
        }
    }
    }
