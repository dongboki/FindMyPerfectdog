package com.sdc.findmyperfectdog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sdc.survey.SizeScreen
import com.sdc.survey.ActivityScreen
import com.sdc.survey.IndependenceScreen
import com.sdc.survey.KidScreen
import com.sdc.survey.ResultScreen
import com.sdc.survey.SheddingScreen
import com.sdc.survey.YardScreen
import com.sdc.survey.TrainLevelScreen



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                SurveyNavHost(navController = navController)
            }
        }
    }
}
val AfacadFontFamily = FontFamily(
    Font(R.font.afacad_medium) // res/font/afacad.ttf에 있는 폰트를 참조
)

// SurveyNavHost: 설문조사에 사용되는 상태들을 remember로 생성하고 각 화면으로 전달
@Composable
fun SurveyNavHost(navController: NavHostController) {
    // 각 설문 항목에 대한 상태 변수
    val selectedSize = remember { mutableStateOf("") }
    val selectedYard = remember { mutableStateOf("") }
    val selectedActivity = remember { mutableStateOf("") }
    val selectedIndependence = remember { mutableStateOf("") }
    val hasKid = remember { mutableStateOf("") }
    val selectedShedding = remember { mutableStateOf("") }
    val selectedTrainlevel = remember { mutableStateOf("") }

    NavHost(navController = navController, startDestination = "size_screen") {
        composable("size_screen") {
            SizeScreen(selectedSize = selectedSize, navController = navController)
        }
        composable("yard_screen") {
            YardScreen(selectedYard = selectedYard, navController = navController)
        }
        composable("activity_screen") {
            ActivityScreen(selectedActivity = selectedActivity, navController = navController)
        }
        composable("independence_screen") {
            IndependenceScreen(selectedIndependence = selectedIndependence, navController = navController)
        }
        composable("kid_screen") {
            KidScreen(hasKid = hasKid, navController = navController)
        }
        composable("trainLevel_screen") {
            TrainLevelScreen(selectedTrainLevel = selectedTrainlevel, navController = navController)
        }
        composable("shedding_screen") {
            SheddingScreen(selectedShedding = selectedShedding, navController = navController)
        }


        composable("result_screen") {
            ResultScreen(
                selectedSize = selectedSize.value,
                selectedYard = selectedYard.value,
                selectedActivity = selectedActivity.value,
                selectedIndependence = selectedIndependence.value,
                hasKid = hasKid.value,
                selectedShedding = selectedShedding.value,
                selectedTrainlevel = selectedTrainlevel.value
            )
        }
    }
}