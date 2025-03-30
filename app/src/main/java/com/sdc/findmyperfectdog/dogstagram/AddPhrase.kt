package com.sdc.findmyperfectdog.dogstagram

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPhraseScreen(navController: NavController) {
    // 이전 화면에서 저장한 데이터 가져오기
    val savedStateHandle = navController.previousBackStackEntry?.savedStateHandle
    val nickname = savedStateHandle?.get<String>("nickname") ?: ""
    val password = savedStateHandle?.get<String>("password") ?: ""
    val imageUri = savedStateHandle?.get<String>("imageUri") // null 가능

    var phrase by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "문구 입력",
            modifier = Modifier.padding(vertical = 16.dp)
        )

        OutlinedTextField(
            value = phrase,
            onValueChange = { phrase = it },
            label = { Text("문구 추가...") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray,
                    contentColor = Color.Black
                )
            ) {
                Text("취소")
            }

            Button(
                onClick = {
                    // 최종적으로 게시글 생성
                    val newPost = AnonymousPost(
                        nickname = nickname,
                        password = password,
                        imageUri = imageUri,
                        phrase = phrase,  // 입력한 문구
                        createdAt = System.currentTimeMillis()
                    )
                    // Firebase 업로드
                    // 만약 이미지 업로드 과정이 별도로 필요한 경우, 이미 Create화면에서 업로드한 상태라면
                    // 여기서는 바로 Firestore 업로드
                    FirebaseRepository.uploadPostToFirestore(
                        post = newPost,
                        onSuccess = {
                            navController.navigate("AnonymousPostList_screen") {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo("AnonymousPostList_screen") { inclusive = true }
                            }
                        },
                        onFailure = {
                            // 오류 처리
                        }
                    )
                }
            ) {
                Text("등록")
            }
        }
    }
}
