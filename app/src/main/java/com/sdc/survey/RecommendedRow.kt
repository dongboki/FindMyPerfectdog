package com.sdc.survey

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sdc.findmyperfectdog.PretenderFontFamily
import coil.compose.AsyncImage

// 2~5순위 강아지 가로 스크롤
@Composable
fun RecommendedRow(
    breeds: List<Breed>,
    onBreedClick: (Breed) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
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
                AsyncImage(
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
                    maxLines = 2, // 한 줄 대신 최대 2줄까지 허용
                    overflow = TextOverflow.Ellipsis, // 2줄을 초과하면 말줄임 처리
                    modifier = Modifier.fillMaxWidth() // Column 내 가용 너비 모두 사용
                )
            }
        }
    }
}
