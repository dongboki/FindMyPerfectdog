package com.sdc.survey

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore

// Firestore에서 불러온 데이터를 담을 데이터 클래스 (기본 생성자 필수)
data class Breed(
    val name: String = "",
    val size: String = "",
    val yard: String = "",
    val activity: String = "",
    val independence: String = "",
    val kid: String = "",
    val youngthumbnail: String = "",
    val oldthumbnail: String = ""
)

// Firestore에서 모든 Breed 문서를 가져온 후, 조건에 따른 일치 점수를 계산하여
// 가장 높은 점수를 가진 강아지 한 개를 선택하는 함수
fun fetchBestMatchingBreedFromFirestore(
    selectedSize: String,
    hasYard: String,
    selectedActivity: String,
    selectedIndependence: String,
    hasKid: String,
    onSuccess: (Breed?) -> Unit,
    onFailure: (Exception) -> Unit
) {
    val firestore = FirebaseFirestore.getInstance()

    firestore.collection("1breed")
        .get()
        .addOnSuccessListener { querySnapshot ->
            // 각 Breed와 일치 점수를 Pair로 저장 (Breed, score)
            val breedScores = mutableListOf<Pair<Breed, Int>>()
            for (document in querySnapshot.documents) {
                val breed = document.toObject(Breed::class.java)
                if (breed != null) {
                    var score = 0
                    if (breed.size == selectedSize) score++
                    if (breed.yard == hasYard) score++
                    if (breed.activity == selectedActivity) score++
                    if (breed.independence == selectedIndependence) score++
                    if (breed.kid == hasKid) score++
                    breedScores.add(Pair(breed, score))
                }
            }
            // 가장 높은 점수를 가진 Breed 선택 (여러 개일 경우 첫 번째)
            val bestMatch = breedScores.maxByOrNull { it.second }?.first
            onSuccess(bestMatch)
        }
        .addOnFailureListener { e ->
            onFailure(e)
        }
}

// Firestore에서 데이터를 가져와 화면에 표시하는 Composable
@Composable
fun ResultScreen(
    selectedSize: String,
    hasYard: String,
    selectedActivity: String,
    selectedIndependence: String,
    hasKid: String
) {
    // 가장 일치율이 높은 Breed (최종 결과)
    val bestBreed = remember { mutableStateOf<Breed?>(null) }
    // 로딩 상태 / 에러 메시지
    val isLoading = remember { mutableStateOf(true) }
    val errorMessage = remember { mutableStateOf<String?>(null) }

    // 화면 구성 시 Firestore 호출
    LaunchedEffect(selectedSize, hasYard, selectedActivity, selectedIndependence, hasKid) {
        isLoading.value = true
        errorMessage.value = null

        fetchBestMatchingBreedFromFirestore(
            selectedSize = selectedSize,
            hasYard = hasYard,
            selectedActivity = selectedActivity,
            selectedIndependence = selectedIndependence,
            hasKid = hasKid,
            onSuccess = { breed ->
                bestBreed.value = breed
                isLoading.value = false
            },
            onFailure = { e ->
                errorMessage.value = e.message
                isLoading.value = false
            }
        )
    }

    // UI 구성
    when {
        isLoading.value -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        errorMessage.value != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text(text = "오류 발생: ${errorMessage.value}")
            }
        }
        else -> {
            if (bestBreed.value == null) {
                // 조건에 맞는 강아지가 없는 경우
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Text(text = "조건에 맞는 강아지가 없습니다.")
                }
            } else {
                // 가장 일치율이 높은 강아지 한 개만 표시
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    BreedItem(breed = bestBreed.value!!)
                }
            }
        }
    }
}

// Breed 하나를 표시하는 Composable
@Composable
fun BreedItem(breed: Breed) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Text(text = "견종 이름: ${breed.name}", fontWeight = FontWeight.Bold)
        Text(text = "사이즈: ${breed.size}")
        Text(text = "마당 유무: ${breed.yard}")
        Text(text = "활동량: ${breed.activity}")
        Text(text = "독립성: ${breed.independence}")
        Text(text = "아이 유무: ${breed.kid}")
        Text(text = "어린 썸네일 URL: ${breed.youngthumbnail}")
        Text(text = "성견 썸네일 URL: ${breed.oldthumbnail}")
        Divider(modifier = Modifier.padding(top = 8.dp))
    }
}
