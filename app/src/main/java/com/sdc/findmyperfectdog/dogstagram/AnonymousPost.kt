package com.sdc.findmyperfectdog.dogstagram

data class AnonymousPost(
    val id: String = "",
    val nickname: String = "",
    val password: String = "",
    val imageUri: String? = null,
    val phrase: String? = null,
    val createdAt: Long = 0L,
    val likeCount: Int = 0
)
