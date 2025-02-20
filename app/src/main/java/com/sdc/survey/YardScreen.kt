package com.sdc.survey

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color

// 2. 마당 유무 화면 (4가지 주거환경 선택 화면)
// 사용자에게는 "원룸/오피스텔" 같은 긴 설명을 보여주지만,
// 실제로는 "소형", "중형", "대형", "농가" 중 하나를 selectedYard에 저장함.
@Composable
fun YardScreen(selectedYard: MutableState<String>, navController: NavController) {

    // (1) 긴 설명 -> 짧은 분류 매핑
    val yardMapping = mapOf(
        "원룸/오피스텔 (야외공간 없음)" to "소형",
        "아파트/빌라/다세대 (제한된 야외공간)" to "중형",
        "단독주택/전원주택 (정원/마당 있음)" to "대형",
        "농가 (넓은 야외공간 있음)" to "농가"
    )

    // (2) 사용자에게 보여줄 옵션(긴 설명) 목록
    val yardOptions = yardMapping.keys.toList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .systemBarsPadding()
    ) {
        Text(
            text = "2.  주거환경",
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        // 라디오 버튼: yardOptions 각각 표시
        yardOptions.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                // 현재 selectedYard.value 와 yardMapping[option]이 같은지 비교
                RadioButton(
                    selected = (yardMapping[option] == selectedYard.value),
                    onClick = {
                        // "원룸/오피스텔 (야외공간 없음)" -> "소형"
                        val shortValue = yardMapping[option]
                        if (shortValue != null) {
                            selectedYard.value = shortValue
                        }
                    }
                )
                // 사용자에게는 긴 설명을 그대로 보여줌
                Text(
                    text = option,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // 다음 버튼
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .border(1.dp, Color(0xFF999999), RoundedCornerShape(12.dp))
        ) {
            Button(
                onClick = {
                    // 다음 화면("activity_screen")으로 이동
                    navController.navigate("activity_screen")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                // "소형"/"중형"/"대형"/"농가" 중 하나가 selectedYard.value에 있어야만 활성화
                enabled = selectedYard.value.isNotEmpty()
            ) {
                Text(
                    text = "다음",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
