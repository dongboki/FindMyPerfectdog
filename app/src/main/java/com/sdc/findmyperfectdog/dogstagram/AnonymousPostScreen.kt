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
    var passwordError by remember { mutableStateOf("") }


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
        Spacer(modifier = Modifier.height(8.dp))

        if (passwordError.isNotEmpty()) {
            Text(
                text = passwordError,
                color = Color.Red,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Start
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
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
                    passwordError = ""
                    if (nickname.isBlank() || password.isBlank()) {
                        uploadError = "닉네임과 비밀번호를 입력해주세요."
                        return@Button
                    }
                    if (password.length < 5) {
                        passwordError = "비밀번호는 5글자 이상이어야 합니다."
                        return@Button
                    }

                    isUploading = true
                    if (selectedImageUri != null) {
                        FirebaseRepository.uploadImageToStorage(
                            imageUri = selectedImageUri!!,
                            onSuccess = { downloadUrl ->
                                isUploading = false
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
                        isUploading = false
                        navController.currentBackStackEntry?.savedStateHandle?.apply {
                            set("nickname", nickname)
                            set("password", password)
                            set("imageUri", null)
                        }
                        navController.navigate("AddPhrase_screen")
                    }
                }
            ) {
                Text("다음")
            }
        }
    }
}
