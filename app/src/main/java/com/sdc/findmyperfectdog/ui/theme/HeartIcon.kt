package com.sdc.findmyperfectdog.ui.theme


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Text
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sdc.findmyperfectdog.R
import com.sdc.findmyperfectdog.dogstagram.AnonymousPost
import com.sdc.findmyperfectdog.dogstagram.FirebaseRepository

@Composable
fun LikeRow(
    post: AnonymousPost,
    onLikeUpdated: (newCount: Int) -> Unit = {}
) {
    var likeCount by remember { mutableStateOf(post.likeCount) }
    var hasLiked by remember { mutableStateOf(false) } // 좋아요 상태

    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.drawable.like),
            contentDescription = "Like",
            modifier = Modifier
                .size(20.dp)
                .clickable {
                    if (!hasLiked) {
                        // 좋아요 상태로 전환: 좋아요 수 증가
                        val newCount = likeCount + 1
                        FirebaseRepository.updateLikeCount(
                            postId = post.id,
                            newCount = newCount,
                            onSuccess = {
                                likeCount = newCount
                                hasLiked = true
                                onLikeUpdated(newCount)
                            },
                            onFailure = {
                                // 에러 처리
                            }
                        )
                    } else {
                        // 이미 좋아요 상태: 좋아요 취소 → 좋아요 수 감소 (최소 0)
                        val newCount = if (likeCount > 0) likeCount - 1 else 0
                        FirebaseRepository.updateLikeCount(
                            postId = post.id,
                            newCount = newCount,
                            onSuccess = {
                                likeCount = newCount
                                hasLiked = false
                                onLikeUpdated(newCount)
                            },
                            onFailure = {
                                // 에러 처리
                            }
                        )
                    }
                }
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "$likeCount",
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}





