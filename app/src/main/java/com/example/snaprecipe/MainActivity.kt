package com.example.snaprecipe

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.snaprecipe.ui.Screens.ResultScreen
import com.example.snaprecipe.ui.theme.SnapRecipeTheme
import com.example.snaprecipe.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SnapRecipeTheme(
                darkTheme = true,
                dynamicColor = false
            ) {
                ResultScreen(
                    resultText = viewModel.uiState,
                    onTakePhoto = {
                        Toast.makeText(this, "Camera screen coming next", Toast.LENGTH_SHORT).show()
                    },
                    onUploadImage = {
                        Toast.makeText(this, "Upload flow coming next", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}