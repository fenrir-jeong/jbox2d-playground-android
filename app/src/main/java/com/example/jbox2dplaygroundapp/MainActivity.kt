package com.example.jbox2dplaygroundapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import com.example.jbox2dplaygroundapp.ui.theme.Jbox2dPlaygroundAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Jbox2dPlaygroundAppTheme {
                InitialScreen()
            }
        }
    }
}