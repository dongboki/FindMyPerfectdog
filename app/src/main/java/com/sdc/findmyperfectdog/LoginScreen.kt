package com.sdc.findmyperfectdog

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sdc.findmyperfectdog.ui.theme.LoginDotsIndicator


val PretenderFontFamily = FontFamily(
    Font(R.font.pretendard_regular,)
    // 다른 굵기가 있다면 여기서 추가
)


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LoginScreen(navController: NavController) {
    val pages = listOf(
        Pair(R.drawable.ic_logindog, "Find Your Dog" to "당신의 라이프 스타일에 딱 맞는\n강아지를 추천해드립니다."),
        Pair(R.drawable.ic_adoption, "Best Companion" to "당신과 함께할 완벽한 반려견을 찾아보세요."),
        Pair(R.drawable.ic_petstore, "Premium Pet Shop" to "소중한 반려견을 위한 특별한 쇼핑을 시작해보세요!")
    )

    val pagerState = rememberPagerState { pages.size } // 총 페이지 개수 설정

    Column(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(150.dp)) // 상단 여백 조정

        // 🔹 HorizontalPager로 이미지 + 텍스트 슬라이더 적용
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp) // 이미지 + 텍스트 높이 조절
        ) { page ->
            val (imageRes, textContent) = pages[page]
            val (title, subtitle) = textContent

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp), // 이미지 크기 조정
                )
                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = title,
                    fontSize = 34.sp,
                    lineHeight = 44.sp,
                    letterSpacing = (-0.4).sp,
                    fontFamily = PretenderFontFamily
                )
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = subtitle,
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    fontFamily = PretenderFontFamily,
                    textAlign = TextAlign.Center
                )
            }
        }
        // 남은 공간을 채워 버튼을 아래로 밀어줍니다.
        Spacer(modifier = Modifier.weight(1f))

        LoginDotsIndicator(
            totalDots = pages.size,
            selectedIndex = pagerState.currentPage
        )

        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { navController.navigate("home_screen") },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFCA651)),
            modifier = Modifier.fillMaxWidth().height(60.dp)
        ) {
            Text(
                text = "시작하기",
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 44.sp,
                fontFamily = PretenderFontFamily
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}
