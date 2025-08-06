package com.example.jbox2dplaygroundapp

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun MaracasScreen() {
    val context = LocalContext.current
    val world = remember { PhysicsWorld() }
    val scale = 50f

    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }

    val frameCount = remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        world.createWorldBounds(screenWidthPx, screenHeightPx)
        world.initialParticle()
    }

    val shakeSensorManager = remember {
        ShakeSensorManager(context) { fx, fy ->
            world.applyForceToAll(fx, fy)
        }
    }

    DisposableEffect(Unit) {
        shakeSensorManager.register()
        onDispose {
            shakeSensorManager.unregister()
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            world.step()
            frameCount.value++
            delay(16L)
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val dummyValue = frameCount.value

        for (particle in world.particles) {
            drawCircle(
                color = Color.White,
                radius = particle.radius * scale,
                center = Offset(
                    particle.body.position.x * scale,
                    particle.body.position.y * scale
                )
            )
        }
    }
}
