package com.sdc.findmyperfectdog.dictionaryUi

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.sdc.findmyperfectdog.dictionaryData.DictionaryDogBreed

@Composable
fun BreedDetailScreen(breed: DictionaryDogBreed) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // 1. 이미지
        Image(
            painter = rememberAsyncImagePainter(breed.imageUrl ?: ""),
            contentDescription = breed.name ?: "강아지 이미지",
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 2. 이름
        Text(
            text = breed.name ?: "이름 없음",
            style = MaterialTheme.typography.headlineSmall,
            color = Color(0xFFBA0C2F)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 3. 설명
        Text(
            text = breed.description ?: "설명이 없습니다.",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 4. 공식 명칭
        Text(
            text = "공식 명칭: ${breed.officialName ?: "정보 없음"}",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 5. Traits + Notice
        TraitsSectionWithNotice(
            traits = breed.traits ?: emptyMap(),
            notice = breed.notice ?: ""
        )
    }
}
