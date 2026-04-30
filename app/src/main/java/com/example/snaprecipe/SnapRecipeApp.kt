package com.example.snaprecipe

import android.app.Application
import com.example.snaprecipe.utils.AppContextProvider

class SnapRecipeApp : Application() {

    override fun onCreate() {
        super.onCreate()
        AppContextProvider.init(this)
    }
}
