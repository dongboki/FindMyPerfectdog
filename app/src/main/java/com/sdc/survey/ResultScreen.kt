package com.sdc.survey

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable // <-- 새로 추가된 import
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
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import com.sdc.findmyperfectdog.AfacadFontFamily
import com.sdc.findmyperfectdog.ui.theme.DotsIndicator
import com.sdc.findmyperfectdog.ui.theme.ToggleFavoriteIcon

// Firestore에서 불러온 데이터를 담을 데이터 클래스 (기본 생성자 필수)
data class Breed(
    val name: String = "",
    val size: String = "",
    val yard: String = "",
    val activity: String = "",
    val independence: String = "",
    val kid: String = "",
    val shedding: String = "",
    val trainlevel: String = "",
    val youngthumbnail: String = "",
    val oldthumbnail: String = ""
)

fun fetchTopMatchingBreedsFromFirestore(
    selectedSize: String,
    hasYard: String,
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

                    // 크기가 일치하면 +2
                    if (breed.size == selectedSize) score += 2
                    // 아이 유무가 일치하면 +3
                    if (breed.kid == hasKid) score += 3
                    // 마당 유무가 일치하면 +1
                    if (breed.yard == hasYard) score++
                    // 활동량이 일치하면 +1
                    if (breed.activity == selectedActivity) score++
                    // 독립성이 일치하면 +1
                    if (breed.independence == selectedIndependence) score++
                    // 추가: 털빠짐(shedding)이 일치하면 +1
                    if (breed.shedding == selectedShedding) score++
                    // 추가: 훈련 난이도(trainlevel)가 일치하면 +1
                    if (breed.trainlevel == selectedTrainlevel) score++


                    breedScores.add(breed to score)
                }
            }
            // 점수를 내림차순으로 정렬 후 상위 5개 선택
            val topFive = breedScores.sortedByDescending { it.second }
                .take(5)
                .map { it.first }
            onSuccess(topFive)
        }
        .addOnFailureListener { e ->
            onFailure(e)
        }
}


// Firestore에서 데이터를 가져와 UI에 상위 5개 강아지를 순위와 함께 표시하는 Composable
@Composable
fun ResultScreen(
    selectedSize: String,
    hasYard: String,
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
        hasYard,
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
            hasYard = hasYard,
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
                    // 1순위 보여주는 BreedItem
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
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // 여기서 RecommendedRow 호출 + 클릭 콜백 구현
                        RecommendedRow(otherBreeds) { clickedBreed ->
                            // 클릭된 강아지를 1순위로 교체하는 로직
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
        // 조건에 따라 견종 이름 텍스트 스타일 분기
        if (isFirstRank) {
            Text(
                text = breed.name,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = subTitleText,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(17.dp))

        } else {
            Text(
                text = breed.name,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        if (isFirstRank) {
            // 1순위일 때: 큰 카드 + LazyRow로 이미지 2장, 아이콘/좋아요 겹쳐 표시
            androidx.compose.material3.Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .width(361.dp)
                    .height(354.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = androidx.compose.material3.CardDefaults.cardElevation(4.dp)
            ) {
                // LazyListState를 이용해 스크롤 상태 추적
                val listState = rememberLazyListState()
                val flingBehavior = rememberSnapFlingBehavior(listState)

                // Breed가 바뀔 때마다 LazyRow의 스크롤을 0번 인덱스로 초기화함
                LaunchedEffect(breed) {
                    listState.scrollToItem(0)
                }

                // 현재 보이는 아이템 인덱스 (0 또는 1) 추적
                val currentIndex by remember {
                    derivedStateOf { listState.firstVisibleItemIndex }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                ) {
                    // 두 이미지를 가로 스크롤(LazyRow)
                    androidx.compose.foundation.lazy.LazyRow(
                        modifier = Modifier.fillMaxSize(),
                        state = listState,
                        flingBehavior = flingBehavior, // 스크롤 상태 연결

                    ) {
                        // 첫 번째 아이템
                        item {
                            coil.compose.AsyncImage(
                                model = breed.youngthumbnail,
                                contentDescription = "어린 썸네일",
                                modifier = Modifier
                                    .fillParentMaxWidth()
                                    .fillParentMaxHeight(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        // 두 번째 아이템
                        item {
                            coil.compose.AsyncImage(
                                model = breed.oldthumbnail,
                                contentDescription = "성견 썸네일",
                                modifier = Modifier
                                    .fillParentMaxWidth()
                                    .fillParentMaxHeight(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    // Dot 인디케이터: totalDots = 2 (이미지 2장), selectedIndex = currentIndex
                    DotsIndicator(
                        totalDots = 2,
                        selectedIndex = currentIndex,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 20.dp)
                    )

                    // 토글 가능한 하트 아이콘
                    ToggleFavoriteIcon()

                    // 오른쪽 하단 좋아요 수 (예: 2,890)
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
                            text = "1000", // 좋아요 수 예시
                            color = Color(0xFF001A72),
                            fontSize = 16.sp,
                            fontFamily = AfacadFontFamily,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                }
            }
            Text("강아지 정보")
            Spacer(modifier = Modifier.height(8.dp))
        } else {
            // 2~5순위: 기존 방식 그대로
            coil.compose.AsyncImage(
                model = breed.youngthumbnail,
                contentDescription = "어린 썸네일",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            coil.compose.AsyncImage(
                model = breed.oldthumbnail,
                contentDescription = "성견 썸네일",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }
    }
}

// 클릭을 전달받기 위해 onBreedClick 추가
@Composable
fun RecommendedRow(
    breeds: List<Breed>,
    onBreedClick: (Breed) -> Unit
) {
    // LazyRow로 가로 스크롤
    androidx.compose.foundation.lazy.LazyRow(
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
    ) {
        items(breeds.size) { index ->
            val breed = breeds[index]
            // youngthumbnail + 이름만 표시
            Column(
                modifier = Modifier
                    .size(133.dp)  // 카드 너비
                    .padding(vertical = 2.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        // 클릭 시, 상위 Composable에 정의된 콜백 호출
                        onBreedClick(breed)
                    }
            ) {
                // 이미지
                coil.compose.AsyncImage(
                    model = breed.youngthumbnail,
                    contentDescription = breed.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(90.dp)
                        .width(133.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                )
                Spacer(modifier = Modifier.height(8.dp))
                // 이름
                Text(
                    text = breed.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1
                )
            }
        }
    }
}
