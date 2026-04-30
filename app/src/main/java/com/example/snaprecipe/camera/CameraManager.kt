package com.example.snaprecipe.camera

import android.content.Context
import java.io.File

class CameraManager(private val context: Context) {

    fun createImageFile(): File {
        val fileName = "captured_${System.currentTimeMillis()}.jpg"
        return File(context.cacheDir, fileName)
    }
}

