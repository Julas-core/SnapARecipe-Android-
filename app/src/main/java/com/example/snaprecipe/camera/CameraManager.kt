package com.example.snaprecipe.camera

import android.content.Context
import java.io.File

class CameraManager(private val context: Context) {

    fun createImageFile(): File {
        return File(context.cacheDir, "captured.jpg")
    }
}

