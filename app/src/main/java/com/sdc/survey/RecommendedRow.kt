package com.sdc.survey

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sdc.findmyperfectdog.PretenderFontFamily

// 2~5순위 강아지 가로 스크롤
@Composable
fun RecommendedRow(
    breeds: List<Breed>,
    onBreedClick: (Breed) -> Unit
) {
    androidx.compose.foundation.lazy.LazyRow(
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
    ) {
        items(breeds.size) { index ->
            val breed = breeds[index]
            Column(
                modifier = Modifier
                    .size(135.dp)
                    .padding(vertical = 2.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        onBreedClick(breed)
                    }
            ) {
                coil.compose.AsyncImage(
                    model = breed.youngthumbnail,
                    contentDescription = breed.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(135.dp)
                        .height(95.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = breed.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    fontFamily = PretenderFontFamily,
                    maxLines = 1
                )
            }
        }
    }
}
