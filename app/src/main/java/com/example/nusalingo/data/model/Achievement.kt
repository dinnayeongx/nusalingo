package com.example.nusalingo.data.model

data class Achievement(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val unlocked: Boolean = false,
    val progress: Int = 0, // progres saat ini
    val target: Int = 1    // target untuk membuka pencapaian
)
