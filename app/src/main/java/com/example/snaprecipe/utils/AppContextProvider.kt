package com.example.snaprecipe.utils

import android.content.Context

object AppContextProvider {
    private lateinit var context: Context

    fun init(appContext: Context) {
        context = appContext.applicationContext
    }

    fun get(): Context {
        return context
    }
}
