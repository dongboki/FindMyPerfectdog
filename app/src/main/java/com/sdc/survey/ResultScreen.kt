package com.sdc.survey

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore
import com.sdc.findmyperfectdog.AfacadFontFamily
import com.sdc.findmyperfectdog.PretenderFontFamily
import com.sdc.findmyperfectdog.ui.theme.DotsIndicator
import com.sdc.findmyperfectdog.ui.theme.ToggleFavoriteIcon

// Firestore에서 불러온 데이터를 담을 데이터 클래스 (기본 생성자 필수)
data class Breed(
    val name: String = "",
    val size: String = "",
    val home: List<String> = emptyList(),
    val activity: String = "",
    val independence: String = "",
    val kid: String = "",
    val shedding: String = "",
    val trainlevel: String = "",
    val youngthumbnail: String = "",
    val oldthumbnail: String = ""
)

// Firestore에서 데이터를 가져와 점수를 계산해 상위 5개를 반환
fun fetchTopMatchingBreedsFromFirestore(
    selectedSize: String,
    selectedHome: String,
    selectedActivity: String,
    selectedIndependence: String,
    hasKid: String,
    selectedShedding: String,
    selectedTrainlevel: String,
    onSuccess: (List<Breed>) -> Unit,
    onFailure: (Exception) -> Unit
) {
    val firestore = FirebaseFirestore.getInstance()

    firestore.collection("allbreeds")
        .get()
        .addOnSuccessListener { querySnapshot ->
            val breedScores = mutableListOf<Pair<Breed, Int>>()
            for (document in querySnapshot.documents) {
                val breed = document.toObject(Breed::class.java)
                if (breed != null) {
                    // 🔹 초대형견 예외 처리
                    if (selectedSize != "초대형" && breed.size == "초대형") continue

                    var score = 0

                    if (breed.size == selectedSize) {
                        score += 10
                    } else {
                        score -= 5  // 불일치일 때 페널티 -5
                    }
                    // 2) 아이 유무가 일치하면 +3
                    if (breed.kid == hasKid) score += 3
                    // 3) 주거환경(리스트 형태)이면, selectedHome가 breed.home 안에 있으면 +1
                    if (selectedHome in breed.home) {
                        score++
                    }
                    // 4) 활동량
                    if (breed.activity == selectedActivity) score++
                    // 5) 독립성
                    if (breed.independence == selectedIndependence) score++
                    // 6) 털빠짐
                    if (breed.shedding == selectedShedding) score++
                    // 7) 훈련 난이도
                    if (breed.trainlevel == selectedTrainlevel) score++

                    breedScores.add(breed to score)
                }
            }
            // 점수를 내림차순으로 정렬 후 상위 5개
            val topFive = breedScores.sortedByDescending { it.second }
                .take(5)
                .map { it.first }
            onSuccess(topFive)
        }
        .addOnFailureListener { e ->
            onFailure(e)
        }
}

// 상위 5개 강아지를 UI에 표시하는 Composable
@Composable
fun ResultScreen(
    selectedSize: String,
    selectedHome: String,
    selectedActivity: String,
    selectedIndependence: String,
    hasKid: String,
    selectedShedding: String,
    selectedTrainlevel: String
) {
    val topBreeds = remember { mutableStateOf<List<Breed>>(emptyList()) }
    val isLoading = remember { mutableStateOf(true) }
    val errorMessage = remember { mutableStateOf<String?>(null) }



    LaunchedEffect(
        selectedSize,
        selectedHome,
        selectedActivity,
        selectedIndependence,
        hasKid,
        selectedShedding,
        selectedTrainlevel
    ) {
        isLoading.value = true
        errorMessage.value = null

        fetchTopMatchingBreedsFromFirestore(
            selectedSize = selectedSize,
            selectedHome = selectedHome,
            selectedActivity = selectedActivity,
            selectedIndependence = selectedIndependence,
            hasKid = hasKid,
            selectedShedding = selectedShedding,
            selectedTrainlevel = selectedTrainlevel,
            onSuccess = { breeds ->
                topBreeds.value = breeds
                isLoading.value = false
            },
            onFailure = { e ->
                errorMessage.value = e.message
                isLoading.value = false
            }
        )
    }


    when {
        isLoading.value -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        errorMessage.value != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "오류 발생: ${errorMessage.value}")
            }
        }

        else -> {
            if (topBreeds.value.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "조건에 맞는 강아지가 없습니다.")
                }
            } else {
                // 1) 현재 1순위 강아지
                val firstBreed = topBreeds.value.first()

                // 2) 나머지 강아지들(2~5순위)
                val otherBreeds = topBreeds.value.drop(1)

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(22.dp)
                ) {
                    // 1순위 BreedItem
                    BreedItem(
                        breed = firstBreed,
                        isFirstRank = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 추천 강아지가 있을 때만 표시
                    if (otherBreeds.isNotEmpty()) {
                        Text(
                            text = "추천 강아지",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = PretenderFontFamily
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // 나머지를 가로 스크롤로 표시 (RecommendedRow)
                        RecommendedRow(otherBreeds) { clickedBreed ->
                            // 클릭된 강아지를 1순위로 교체
                            val currentList = topBreeds.value
                            val newList = listOf(clickedBreed) + currentList.filter { it != clickedBreed }
                            topBreeds.value = newList
                        }

                    }
                }
            }
        }
    }
}

// Breed 하나를 표시하는 Composable
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BreedItem(breed: Breed, isFirstRank: Boolean = false) {
    val subTitleText = when (breed.size) {
        "대형" -> "‘함께 뛰놀기 딱 좋은 반려견’"
        "중형" -> "‘도시 생활과 자연을 모두 즐길 수 있는 반려견’"
        "소형" -> "‘작지만 큰 기쁨을 주는 반려견, 당신의 소중한 친구’"
        else -> "‘당신 성향에 딱 맞는 반려견’"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // 🔹 강아지 이름과 설명
        if (isFirstRank) {
            Text(
                text = breed.name,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                fontFamily = PretenderFontFamily,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = subTitleText,
                color = Color.Gray,
                fontFamily = PretenderFontFamily
            )
            Spacer(modifier = Modifier.height(17.dp))

            // 🔹 페이지 상태 저장
            val pagerState = rememberPagerState { 2 }

            LaunchedEffect(breed) {
                pagerState.scrollToPage(0)
            }

            androidx.compose.material3.Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .width(361.dp)
                    .height(360.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = androidx.compose.material3.CardDefaults.cardElevation(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                ) {
                    // 🔹 HorizontalPager (이미지 슬라이더)
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        val imageUrl = if (page == 0) breed.youngthumbnail else breed.oldthumbnail
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = if (page == 0) "어린 썸네일" else "성견 썸네일",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    // 🔹 하트 아이콘 (즐겨찾기)
                    ToggleFavoriteIcon()

                    // 🔹 좋아요 수 표시
                    androidx.compose.foundation.layout.Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(20.dp)
                            .height(32.dp)
                            .width(90.dp)
                            .background(
                                color = Color(0xFFFFFFFF),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 10.dp)
                    ) {
                        androidx.compose.material3.Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.ThumbUp,
                            contentDescription = "Likes",
                            tint = Color(0xFF001A72),
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Text(
                            text = "0", // 좋아요 수 예시
                            color = Color(0xFF001A72),
                            fontSize = 16.sp,
                            fontFamily = AfacadFontFamily,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                }
            }

            // 🔹 Dot Indicator를 카드 아래로 이동
            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                DotsIndicator(
                    totalDots = 2,
                    selectedIndex = pagerState.currentPage
                )
            }


            Spacer(modifier = Modifier.height(50.dp))
        } else {
            Text(
                text = breed.name,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                fontFamily = PretenderFontFamily,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // 🔹 2~5순위: 기존 방식 (이미지 2장 표시)
            AsyncImage(
                model = breed.youngthumbnail,
                contentDescription = "어린 썸네일",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            AsyncImage(
                model = breed.oldthumbnail,
                contentDescription = "성견 썸네일",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }
    }
}