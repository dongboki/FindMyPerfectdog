package com.sdc.findmyperfectdog.dictionaryUi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TraitsSectionWithNotice(
    traits: Map<String, Int>,
    notice: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "특징",
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFFBA0C2F),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        traits.forEach { (label, score) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = label,
                    color = Color(0xFFBA0C2F),
                    modifier = Modifier.weight(1f)
                )

                Row {
                    repeat(5) { i ->
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 2.dp)
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(
                                    if (i < score) Color(0xFFBA0C2F)
                                    else Color.LightGray
                                )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = notice,
            style = MaterialTheme.typography.bodySmall,
            color = Color.DarkGray
        )
    }
}
