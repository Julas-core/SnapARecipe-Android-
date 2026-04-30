package com.example.snaprecipe.data.model

data class RecipeCard(
    val title: String = "",
    val intro: String = "",
    val instructions: List<String> = emptyList(),
    val nutrition: String = "",
    val tips: List<String> = emptyList(),
    val servings: String = "",
    val time: String = ""
)
