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

// 3. 활동량 화면
@Composable
fun ActivityScreen(selectedActivity: MutableState<String>, navController: NavController) {
    val activityOptions = listOf("상", "중", "하")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .systemBarsPadding()
    ) {
        Text(
            text = "3. 활동량",
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        activityOptions.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                RadioButton(
                    selected = selectedActivity.value == option,
                    onClick = { selectedActivity.value = option }
                )
                Text(text = option, modifier = Modifier.padding(start = 8.dp))
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { navController.navigate("independence_screen") },
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedActivity.value.isNotEmpty() // 선택되지 않으면 버튼 비활성화
        ) {
            Text(text = "다음")
        }
    }
}
