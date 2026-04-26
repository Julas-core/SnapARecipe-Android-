package com.example.snaprecipe.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snaprecipe.data.repository.RecipeRepository
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val repository = RecipeRepository()

    var uiState by mutableStateOf("Idle")
        private set

    fun analyze(base64: String) {
        uiState = "Loading..."

        viewModelScope.launch {
            try {
                val result = repository.analyzeImage(base64)
                uiState = result
            } catch (e: Exception) {
                uiState = "Error: ${e.message}"
            }
        }
    }
}