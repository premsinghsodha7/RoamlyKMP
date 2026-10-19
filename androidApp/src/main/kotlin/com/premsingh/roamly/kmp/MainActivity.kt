package com.premsingh.roamly.kmp

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import android.os.Bundle
import com.premsingh.roamly.kmp.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { App() }
    }
}
