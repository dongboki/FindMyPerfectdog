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

@Composable
fun SheddingScreen(selectedShedding: MutableState<String>, navController: NavController) {
    // 많음, 보통, 적음 옵션 리스트를 만듭니다.
    val sheddingOptions = listOf("많음", "보통", "적음")

    // 전체 화면을 채우는 Column을 만듭니다.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .systemBarsPadding()
    ) {
        // 제목 텍스트를 표시합니다.
        Text(
            text = "7. 털빠짐 정도",
            fontWeight = FontWeight.Bold
        )
        // 제목과 옵션 사이에 간격을 줍니다.
        Spacer(modifier = Modifier.height(16.dp))

        // 각 옵션에 대해 RadioButton과 텍스트를 Row에 배치합니다.
        sheddingOptions.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                RadioButton(
                    selected = selectedShedding.value == option,
                    onClick = { selectedShedding.value = option }
                )
                Text(text = option, modifier = Modifier.padding(start = 8.dp))
            }
        }

        // 남은 공간을 차지하는 Spacer (버튼을 하단에 고정)
        Spacer(modifier = Modifier.weight(1f))

        // "다음" 버튼: 옵션이 선택되어 있을 때만 활성화되고, 클릭 시 다음 화면 (예: result_screen)으로 이동합니다.
        Button(
            onClick = { navController.navigate("result_screen") },
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedShedding.value.isNotEmpty()
        ) {
            Text(text = "다음")
        }
    }
}
