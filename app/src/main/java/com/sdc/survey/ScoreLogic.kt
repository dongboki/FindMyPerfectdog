package com.sdc.survey

// 사이즈 점수 계산 함수
fun calculateSizeScore(breedSize: String, selectedSize: String): Int {
    return if (breedSize == selectedSize) 6 else 2
}

// 아이 유무 점수 계산 함수
fun calculateKidScore(breedName: String, hasKid: String): Int? {
    // 가드견, 맹견 리스트
    val dangerousBreeds = listOf(
        "체코슬로바키아 울프독",
        "까나리오",
        "도고 아르헨티노",
        "도베르만 핀셔",
        "도사견",
        "로트와일러",
        "사플라니낙",
        "카네 코르소",
        "오브차카"
    )

    return if (hasKid == "예") {
        // 아이가 있는 경우, 위험 견종이면 추천에서 제외 (null 반환)
        if (breedName in dangerousBreeds) null else 2
    } else {
        // 아이가 없는 경우는 무조건 2점을 반환
        2
    }
}



// 주거환경 및 사이즈 조합 점수 계산 함수
fun calculateHomeScore(selectedHome: String, breedSize: String, breedHome: List<String>): Int {
    var score = 0
    if (selectedHome in breedHome) {
        score += 2
    }
    when (selectedHome) {
        "소형" -> if (breedSize == "소형") score += 2
        "중형" -> {
            if (breedSize == "중형") score += 2
            else if (breedSize == "소형") score += 1
        }
        "대형" -> {
            if (breedSize == "대형") score += 2
            else if (breedSize == "중형") score += 1
            else if (breedSize == "소형") score += 1
        }
        "초대형" -> {
            if (breedSize == "초대형") score += 2
            else if (breedSize == "소형") score += 1
            else if (breedSize == "중형") score += 1
            else if (breedSize == "대형") score += 1
        }
    }
    return score
}

// 활동량 점수 계산 함수
fun calculateActivityScore(breedActivity: String, selectedActivity: String): Int {
    return if (breedActivity == selectedActivity) 3 else 1
}

// 독립성 점수 계산 함수
fun calculateIndependenceScore(breedIndependence: String, selectedIndependence: String): Int {
    return if (breedIndependence == selectedIndependence) 2 else 0
}

// 털빠짐 점수 계산 함수
fun calculateSheddingScore(breedShedding: String, selectedShedding: String): Int {
    return if (breedShedding == selectedShedding) 3 else 0
}

// 훈련 난이도 점수 계산 함수
fun calculateTrainLevelScore(breedTrainlevel: String, selectedTrainlevel: String): Int {
    return if (breedTrainlevel == selectedTrainlevel) 2 else 1
}


