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

// 2. 마당 유무 화면
@Composable
fun YardScreen(hasYard: MutableState<String>, navController: NavController) {
    val yesNoOptions = listOf("예", "아니오")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .systemBarsPadding()
    ) {
        Text(
            text = "2. 마당 유무",

            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        yesNoOptions.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                RadioButton(
                    selected = hasYard.value == option,
                    onClick = { hasYard.value = option }
                )
                Text(text = option, modifier = Modifier.padding(start = 8.dp))
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { navController.navigate("activity_screen") },
            modifier = Modifier.fillMaxWidth(),
            enabled = hasYard.value.isNotEmpty()
        ) {
            Text(text = "다음")
        }
    }
}