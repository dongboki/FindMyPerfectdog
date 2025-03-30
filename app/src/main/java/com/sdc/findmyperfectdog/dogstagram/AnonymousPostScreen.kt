package com.sdc.findmyperfectdog.dogstagram

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.sdc.findmyperfectdog.R

@Composable
fun CreateAnonymousPostScreen(navController: NavController) {

    var nickname by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }
    var uploadError by remember { mutableStateOf("") }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            selectedImageUri = uri
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "익명 게시글 작성",
            modifier = Modifier.padding(vertical = 16.dp)
        )

        OutlinedTextField(
            value = nickname,
            onValueChange = { nickname = it },
            label = { Text("닉네임") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("비밀번호") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (selectedImageUri != null) {
                Image(
                    painter = rememberImagePainter(data = selectedImageUri),
                    contentDescription = "선택된 이미지",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.image),
                    contentDescription = "기본 이미지",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                imagePickerLauncher.launch("image/*")
            }
        ) {
            Text("사진 선택하기")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isUploading) {
            Text("업로드 중입니다...", color = Color.Blue)
        }
        if (uploadError.isNotEmpty()) {
            Text(uploadError, color = Color.Red)
        }

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
                    if (nickname.isNotBlank() && password.isNotBlank()) {
                        isUploading = true
                        if (selectedImageUri != null) {
                            // 이미지가 선택되었으면 Firebase Storage에 업로드하여 다운로드 URL 획득
                            FirebaseRepository.uploadImageToStorage(
                                imageUri = selectedImageUri!!,
                                onSuccess = { downloadUrl ->
                                    isUploading = false
                                    // savedStateHandle에 다운로드 URL 저장
                                    navController.currentBackStackEntry?.savedStateHandle?.apply {
                                        set("nickname", nickname)
                                        set("password", password)
                                        set("imageUri", downloadUrl)
                                    }
                                    navController.navigate("AddPhrase_screen")
                                },
                                onFailure = { exception ->
                                    isUploading = false
                                    uploadError = "이미지 업로드 실패: ${exception.localizedMessage}"
                                }
                            )
                        } else {
                            // 이미지가 없으면 null 전달
                            isUploading = false
                            navController.currentBackStackEntry?.savedStateHandle?.apply {
                                set("nickname", nickname)
                                set("password", password)
                                set("imageUri", null)
                            }
                            navController.navigate("AddPhrase_screen")
                        }
                    }
                }
            ) {
                Text("다음")
            }
        }
    }
}
