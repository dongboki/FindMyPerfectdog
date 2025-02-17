package com.sdc.survey

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

//결과화면
@Composable
fun ResultScreen(
    selectedSize: String,
    hasYard: String,
    selectedActivity: String,
    selectedIndependence: String,
    hasKid: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .systemBarsPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "설문 결과",
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "사이즈: $selectedSize")
        Text(text = "마당 유무: $hasYard")
        Text(text = "활동량: $selectedActivity")
        Text(text = "독립성: $selectedIndependence")
        Text(text = "아이 유무: $hasKid")
    }
}