package com.sdc.findmyperfectdog.dogstagram

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.sdc.findmyperfectdog.ComicNeueFamily
import com.sdc.findmyperfectdog.PretenderFontFamily
import com.sdc.findmyperfectdog.ui.theme.BottomNavBar
import com.sdc.findmyperfectdog.ui.theme.LikeRow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnonymousPostListScreen(navController: NavController) {
    val posts = remember { mutableStateListOf<AnonymousPost>() }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }
    var postToDelete by remember { mutableStateOf<AnonymousPost?>(null) }
    var isRefreshing by remember { mutableStateOf(false) }

    // 새로고침 함수
    fun refreshPosts() {
        isRefreshing = true
        FirebaseRepository.loadPostsFromFirestore(
            onSuccess = {
                posts.clear()
                posts.addAll(it)
                isRefreshing = false
                isLoading = false
            },
            onFailure = {
                errorMessage = "게시글을 불러오지 못했어요 🥲"
                isRefreshing = false
                isLoading = false
            }
        )
    }

    // 🔥 Firestore에서 게시글 불러오기 (화면 진입 시 1회만)
    LaunchedEffect(Unit) {
        FirebaseRepository.loadPostsFromFirestore(
            onSuccess = {
                posts.clear()
                posts.addAll(it)
                isLoading = false
            },
            onFailure = {
                errorMessage = "게시글을 불러오지 못했어요 🥲"
                isLoading = false
            }
        )
    }

    // 🔥 삭제 다이얼로그
    if (postToDelete != null) {
        DeletePostDialog(
            post = postToDelete!!,
            onConfirm = { enteredPassword ->
                if (enteredPassword == postToDelete!!.password) {
                    // 🔥 Firestore에서 삭제
                    FirebaseRepository.deletePostFromFirestore(
                        postId = postToDelete!!.id,
                        onSuccess = {
                            posts.remove(postToDelete)
                            postToDelete = null
                            errorMessage = ""
                        },
                        onFailure = {
                            errorMessage = "삭제 실패 😢"
                        }
                    )
                } else {
                    errorMessage = "비밀번호가 일치하지 않습니다."
                }
            },
            onDismiss = {
                postToDelete = null
                errorMessage = ""
            }
        )
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            "TOP Topics",
                            fontFamily = ComicNeueFamily,
                            fontSize = 28.sp,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = Color.LightGray, thickness = 1.dp)
            }
        },
        bottomBar = {
            BottomNavBar(
                onHomeClick = {
                    navController.navigate("AnonymousPostList_screen") {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onBoneClick = { navController.navigate("surveyNavHost")},
                onAddClick = {
                    navController.navigate("CreateAnonymousPost_screen")
                },
                onProfileClick = {}
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            when {
                isLoading -> {
                    Text("로딩 중입니다...", modifier = Modifier.padding(16.dp))
                }

                errorMessage.isNotEmpty() -> {
                    Text(text = errorMessage, color = Color.Red, modifier = Modifier.padding(8.dp))
                }

                else -> {
                    SwipeRefresh(
                        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
                        onRefresh = { refreshPosts() }
                    ) {
                        LazyColumn {
                            items(posts) { post ->
                                AnonymousPostItem(
                                    post = post,
                                    onDelete = { postToDelete = it }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun AnonymousPostItem(
    post: AnonymousPost,
    onDelete: (AnonymousPost) -> Unit
) {
    // 각 게시글 항목 UI
    Box(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(text = "닉네임: ${post.nickname}", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            if (!post.phrase.isNullOrEmpty()) {
                Text(
                    text = "${post.phrase}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 12.dp, bottom = 12.dp)
                )
            }
            if (post.imageUri != null) {
                AsyncImage(
                    model = post.imageUri,
                    contentDescription = "게시글 이미지",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(360.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop,

                    )

            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LikeRow(post = post)
            }
            // 삭제 버튼
            Button(
                onClick = { onDelete(post) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                )
            ) {
                Text(text = "삭제")
            }
        }
    }
}

@Composable
fun DeletePostDialog(
    post: AnonymousPost,
    onConfirm: (enteredPassword: String) -> Unit,
    onDismiss: () -> Unit
) {
    var enteredPassword by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("게시글 삭제") },
        text = {
            Column {
                Text("삭제하려면 비밀번호를 입력하세요:")
                OutlinedTextField(
                    value = enteredPassword,
                    onValueChange = { enteredPassword = it },
                    label = { Text("비밀번호") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(enteredPassword) }) {
                Text("삭제")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}

