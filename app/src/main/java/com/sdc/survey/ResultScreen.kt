package com.sdc.survey

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore
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
    val oldthumbnail: String = "",
    val thirdthumbnail: String = ""
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
                    // 초대형견 예외 처리
                    if (selectedSize != "초대형" && breed.size == "초대형") continue

                    // 아이가 있는 경우, 위험 견종(맹견 계열)은 calculateKidScore에서 null을 반환하므로 건너뜁니다.
                    val kidScore = calculateKidScore(breed.name, hasKid)
                    if (hasKid == "예" && kidScore == null) continue

                    var score = 0
                    // calculateKidScore의 결과가 null이 아닐 경우에만 점수를 추가합니다.
                    if (kidScore != null) {
                        score += kidScore
                    }
                    if (breed.name == "기슈견" || breed.name == "기슈") {
                        score -= 2
                    }

                    // 각 조건별 점수 계산
                    score += calculateSizeScore(breed.size, selectedSize)
                    score += calculateHomeScore(selectedHome, breed.size, breed.home)
                    score += calculateActivityScore(breed.activity, selectedActivity)
                    score += calculateIndependenceScore(breed.independence, selectedIndependence)
                    score += calculateSheddingScore(breed.shedding, selectedShedding)
                    score += calculateTrainLevelScore(breed.trainlevel, selectedTrainlevel)

                    breedScores.add(breed to score)
                }
            }

            // 점수를 내림차순 정렬 (동일 점수인 경우 Firestore에서 가져온 순서를 그대로 유지)
            val sorted = breedScores.sortedByDescending { it.second }
            if (sorted.size <= 5) {
                onSuccess(sorted.map { it.first })
                return@addOnSuccessListener
            }

            // 상위 3위는 고정
            val fixedTop = sorted.take(3)
            val remainder = sorted.drop(3)
            val required = 2  // 최종적으로 5개가 되어야 하므로, remainder에서 2개 선택

            if (remainder.size <= required) {
                onSuccess(fixedTop.map { it.first } + remainder.map { it.first })
                return@addOnSuccessListener
            }

            // remainder에서 tied 그룹 처리: 4번째(전체 순위에서 4번째)의 점수를 기준으로 tied 그룹 결정
            val tieScore = remainder[required - 1].second // remainder[1]의 점수가 tieScore
            val fixedPart = remainder.takeWhile { it.second > tieScore }
            val tieGroup = remainder.drop(fixedPart.size).takeWhile { it.second == tieScore }
            val numberToSelect = required - fixedPart.size
            val selectedFromTie = tieGroup.shuffled().take(numberToSelect)
            val chosen = fixedPart + selectedFromTie

            val finalList = fixedTop + chosen

            onSuccess(finalList.map { it.first })
        }
        .addOnFailureListener { e ->
            onFailure(e)
        }
}


@Composable
fun ResultScreen(
    selectedSize: String,
    selectedHome: String,
    selectedActivity: String,
    selectedIndependence: String,
    hasKid: String,
    selectedShedding: String,
    selectedTrainlevel: String,
    navController: NavController
) {
    val topBreeds = remember { mutableStateOf<List<Breed>>(emptyList()) }
    val isLoading = remember { mutableStateOf(true) }
    val errorMessage = remember { mutableStateOf<String?>(null) }

    BackHandler {
    }

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

                // 전체 화면을 Box로 감싸기
                Box(modifier = Modifier.fillMaxSize()) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 22.dp, end = 22.dp, top = 16.dp)
                    ) {
                        // 1순위 강아지
                        Box(modifier = Modifier.weight(0.7f)) {
                            BreedItem(
                                breed = firstBreed,
                                isFirstRank = true
                            )
                        }
                        // 원하는 30.dp 간격
                        Spacer(modifier = Modifier.height(30.dp))

                        // 추천 강아지 섹션
                        if (otherBreeds.isNotEmpty()) {
                            Text(
                                text = "추천 강아지",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = PretenderFontFamily
                            )
                            Box(
                                modifier = Modifier
                                    .weight(0.25f)
                                    .fillMaxWidth()
                            ) {
                                RecommendedRow(otherBreeds) { clickedBreed ->
                                    val currentList = topBreeds.value
                                    val newList = listOf(clickedBreed) + currentList.filter { it != clickedBreed }
                                    topBreeds.value = newList
                                }
                            }
                        }
                        // 버튼 위 Spacer 제거
                        Button(
                            onClick = { navController.navigate("Login_screen") },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFCA651)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)

                        ) {
                            Text(
                                text = "다시하기",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                lineHeight = 24.sp,
                                fontFamily = PretenderFontFamily
                            )
                        }
                        // 하단 여백 유지 (필요 시 조정)
                        Spacer(modifier = Modifier.height(10.dp))
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
            val pagerState = rememberPagerState { 3 }

            LaunchedEffect(breed) {
                pagerState.scrollToPage(0)
            }

            androidx.compose.material3.Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .width(361.dp)
                    .height(355.dp),
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
                        when (page) {
                            0 -> {
                                // 첫 번째 페이지 - 어린 썸네일
                                AsyncImage(
                                    model = breed.youngthumbnail,
                                    contentDescription = "어린 썸네일",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            1 -> {
                                // 두 번째 페이지 - 성견 썸네일
                                AsyncImage(
                                    model = breed.oldthumbnail,
                                    contentDescription = "성견 썸네일",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            // 세 번째 페이지 - 예쁜 이미지
                            2 -> {
                                AsyncImage(
                                    model = breed.thirdthumbnail    ,
                                    contentDescription = "추가 이미지",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }


//                    🔹 하트 아이콘 (즐겨찾기)
                    ToggleFavoriteIcon()

                    // 🔹 좋아요 수 표시
//                    androidx.compose.foundation.layout.Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier
//                            .align(Alignment.BottomEnd)
//                            .padding(20.dp)
//                            .height(32.dp)
//                            .width(90.dp)
//                            .background(
//                                color = Color(0xFFFFFFFF),
//                                shape = RoundedCornerShape(16.dp)
//                            )
//                            .padding(horizontal = 10.dp)
//                    ) {
//                        androidx.compose.material3.Icon(
//                            imageVector = androidx.compose.material.icons.Icons.Default.ThumbUp,
//                            contentDescription = "Likes",
//                            tint = Color(0xFF001A72),
//                            modifier = Modifier.size(13.dp)
//                        )
//                        Spacer(modifier = Modifier.width(14.dp))
//                        Text(
//                            text = "0", // 좋아요 수 예시
//                            color = Color(0xFF001A72),
//                            fontSize = 16.sp,
//                            fontFamily = AfacadFontFamily,
//                            modifier = Modifier.padding(end = 8.dp)
//                        )
//                    }
                }
            }

            // 🔹 Dot Indicator를 카드 아래로 이동
            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                DotsIndicator(
                    totalDots = 3,
                    selectedIndex = pagerState.currentPage
                )
            }

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

