package com.sdc.findmyperfectdog.dogstagram

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import java.util.*

object FirebaseRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    fun uploadImageToStorage(
        imageUri: Uri,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val fileName = UUID.randomUUID().toString()
        val imageRef = storage.reference.child("post_images/$fileName")

        imageRef.putFile(imageUri)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception ?: Exception("이미지 업로드 실패")
                }
                imageRef.downloadUrl
            }
            .addOnSuccessListener { uri ->
                onSuccess(uri.toString())
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun uploadPostToFirestore(
        post: AnonymousPost,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestore.collection("anonymous_posts")
            .add(post)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    fun loadPostsFromFirestore(
        onSuccess: (List<AnonymousPost>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestore.collection("anonymous_posts")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { snapshot ->
                val posts = snapshot.documents.mapNotNull { doc ->
                    val post = doc.toObject(AnonymousPost::class.java)
                    post?.copy(id = doc.id) // 🔥 문서 ID 저장
                }
                onSuccess(posts)
            }
            .addOnFailureListener { onFailure(it) }
    }
    //🔥 삭제하는 함수
    fun deletePostFromFirestore(
        postId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestore.collection("anonymous_posts")
            .document(postId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }
// 🔥좋아요 업데이트 하는 함수
    fun updateLikeCount(
        postId: String,
        newCount: Int,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestore.collection("anonymous_posts")
            .document(postId)
            .update("likeCount", newCount)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

}




