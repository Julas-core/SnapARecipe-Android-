package com.example.snaprecipe.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snaprecipe.data.model.RecipeCard
import com.example.snaprecipe.data.repository.RecipeRepository
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val repository = RecipeRepository()

    var uiState by mutableStateOf("Idle")
        private set

    var recipe by mutableStateOf<RecipeCard?>(null)
        private set

    fun analyze(base64: String, language: String, languageCode: String) {
        uiState = "Loading..."
        recipe = null

        viewModelScope.launch {
            try {
                val result = repository.analyzeImage(base64, language, languageCode)
                recipe = result
                uiState = "Done"
            } catch (e: Exception) {
                uiState = "Error: ${e.message}"
            }
        }
    }

    fun setError(message: String) {
        uiState = "Error: $message"
        recipe = null
    }

    fun reset() {
        uiState = "Idle"
        recipe = null
    }
}