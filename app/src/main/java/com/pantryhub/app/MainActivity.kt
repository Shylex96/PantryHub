package com.pantryhub.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

// AppCompatActivity (not ComponentActivity) is required for per-app language switching
// (AppCompatDelegate.setApplicationLocales) to work on Android below API 33.
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PantryHubApp()
        }
    }
}
