package com.sdc.survey

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sdc.findmyperfectdog.PretenderFontFamily
import com.sdc.findmyperfectdog.ui.theme.BackIcon
import com.sdc.findmyperfectdog.ui.theme.Check
import com.sdc.findmyperfectdog.ui.theme.CustomRadioButton

// 5. 아이 유무 화면
@Composable
fun KidScreen(hasKid: MutableState<String>, navController: NavController) {
    val yesNoOptions = listOf("예", "아니오")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .systemBarsPadding()
    ) {
        BackIcon(navController = navController, step = 5)
        Text(
            text = "5. 아이 유무",
            fontWeight = FontWeight.Bold,
            fontFamily = PretenderFontFamily,
            fontSize = 16.sp
            )
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp) // 버튼 간격 24.dp
        ) {
            yesNoOptions.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    CustomRadioButton(
                        selected = hasKid.value == option,
                        onClick = { hasKid.value = option }
                    )
                    Text(
                        text = option,
                        modifier = Modifier.padding(start = 8.dp),
                        fontFamily = PretenderFontFamily
                    )
                }
            }
        }
        Check(selected = hasKid.value.isNotEmpty(), text = "집에 아이나 반려동물이 있다면 “예”를 체크해주세요.")

        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp) // 높이 60dp 설정
                .border(1.dp, Color(0xFF999999), RoundedCornerShape(12.dp)) // 보더 추가
        ) {
            Button(
                onClick = { navController.navigate("trainLevel_screen") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp), // 버튼 높이도 60dp로 맞춤
                shape = RoundedCornerShape(12.dp), // 둥근 모서리 설정
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White, // 버튼 배경색 흰색
                    contentColor = Color.Black // 버튼 글씨색 검정
                ),
                enabled = hasKid.value.isNotEmpty()
            ) {
                Text(
                    text = "다음",
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = PretenderFontFamily// 글씨 굵기 SemiBold
                )
            }
        }
    }
}