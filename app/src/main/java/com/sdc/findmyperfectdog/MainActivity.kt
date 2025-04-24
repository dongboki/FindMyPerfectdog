package com.sdc.findmyperfectdog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sdc.findmyperfectdog.dictionaryData.DictionaryDogBreed
import com.sdc.findmyperfectdog.dictionaryData.DogDatabase
import com.sdc.findmyperfectdog.dictionaryData.loadDogBreedsFromJson
import com.sdc.findmyperfectdog.dictionaryUi.BreedDetailScreen
import com.sdc.findmyperfectdog.dictionaryUi.BreedListScreen
import com.sdc.survey.SizeScreen
import com.sdc.survey.ActivityScreen
import com.sdc.survey.HomeScreen
import com.sdc.survey.IndependenceScreen
import com.sdc.survey.KidScreen
import com.sdc.survey.ResultScreen
import com.sdc.survey.SheddingScreen
import com.sdc.survey.TrainLevelScreen
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val context = LocalContext.current
                val scope = rememberCoroutineScope()
                val dao = remember { DogDatabase.getDatabase(context).dogBreedDao() }

                // ✅ JSON 데이터를 Room에 한 번만 저장
                LaunchedEffect(Unit) {
                    scope.launch {
                        val current = dao.getAll()
                        if (current.isEmpty()) {
                            val breeds = loadDogBreedsFromJson(context)
                            dao.insertAll(breeds)
                        }
                    }
                }

                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "breed_list") {
                    // ✅ 리스트 화면
                    composable("breed_list") {
                        BreedListScreen(
                            onItemClick = { selectedBreed ->
                                navController.currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("selectedBreed", selectedBreed)

                                navController.navigate("breed_detail")
                            }
                        )
                    }

                    // ✅ 상세 페이지 화면
                    composable("breed_detail") {
                        val breed = navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.get<DictionaryDogBreed>("selectedBreed")

                        breed?.let {
                            BreedDetailScreen(breed = it)
                        }
                    }
                }
            }
        }
    }
}


val AfacadFontFamily = FontFamily(
    Font(R.font.afacad_medium) // res/font/afacad.ttf에 있는 폰트를 참조
)

//// SurveyNavHost: 설문조사에 사용되는 상태들을 remember로 생성하고 각 화면으로 전달
//@Composable
//fun SurveyNavHost(navController: NavHostController) {
//    // 각 설문 항목에 대한 상태 변수
//    val selectedSize = remember { mutableStateOf("") }
//    val selectedHome = remember { mutableStateOf("") }
//    val selectedActivity = remember { mutableStateOf("") }
//    val selectedIndependence = remember { mutableStateOf("") }
//    val hasKid = remember { mutableStateOf("") }
//    val selectedShedding = remember { mutableStateOf("") }
//    val selectedTrainlevel = remember { mutableStateOf("") }
//
//    NavHost(navController = navController, startDestination = "login_screen") {
//        composable("login_screen") {
//            LoginScreen( navController = navController)
//        }
//        composable("home_screen") {
//            HomeScreen(selectedHome = selectedHome, navController = navController)
//        }
//        composable("size_screen") {
//            SizeScreen(selectedSize = selectedSize, navController = navController)
//        }
//        composable("activity_screen") {
//            ActivityScreen(selectedActivity = selectedActivity, navController = navController)
//        }
//        composable("independence_screen") {
//            IndependenceScreen(selectedIndependence = selectedIndependence, navController = navController)
//        }
//        composable("kid_screen") {
//            KidScreen(hasKid = hasKid, navController = navController)
//        }
//        composable("trainLevel_screen") {
//            TrainLevelScreen(selectedTrainLevel = selectedTrainlevel, navController = navController)
//        }
//        composable("shedding_screen") {
//            SheddingScreen(selectedShedding = selectedShedding, navController = navController)
//        }
//
//
//        composable("result_screen") {
//            ResultScreen(
//                selectedSize = selectedSize.value,
//                selectedHome = selectedHome.value,
//                selectedActivity = selectedActivity.value,
//                selectedIndependence = selectedIndependence.value,
//                hasKid = hasKid.value,
//                selectedShedding = selectedShedding.value,
//                selectedTrainlevel = selectedTrainlevel.value
//                ,navController = navController
//            )
//        }
//    }
//}