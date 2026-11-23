package com.example.nusalingo.data.model

data class UserProfile(
    val id: String = "",
    val username: String = "",
    val handle: String = "",
    val avatarUrl: String? = null,
    val joinDate: String = "", // contoh: "September 2026"
    val isGuest: Boolean = false
)
