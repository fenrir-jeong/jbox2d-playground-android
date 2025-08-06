package com.example.jbox2dplaygroundapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun InitialScreen() {
    var showMaracasScreen by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().background(color = Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (showMaracasScreen) {
            Box(contentAlignment = Alignment.Center) {
                GreenButton(text = "Start Again", onClick = { showMaracasScreen = false })
                MaracasScreen()
            }
        } else {
            GreenButton(text = "Click me!", onClick = { showMaracasScreen = true })
        }
    }
}

@Composable
private fun GreenButton(text: String, onClick: () -> Unit) {
    Button(
        colors = ButtonDefaults.buttonColors()
            .copy(containerColor = Color(0xffee8e46), contentColor = Color.White),
        onClick = onClick
    ) {
        Text(text = text)
    }
}