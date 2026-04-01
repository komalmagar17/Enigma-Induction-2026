package com.example.quicknotes.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onAnimationFinished: () -> Unit) {
    val dark = isSystemInDarkTheme()
    var phase by remember { mutableIntStateOf(0) }

    val scale by animateFloatAsState(
        targetValue = if (phase >= 1) 1f else 0.3f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumLow,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    val rotation by animateFloatAsState(
        targetValue = if (phase >= 1) 0f else -180f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumLow,
            stiffness = Spring.StiffnessLow
        ),
        label = "rotation"
    )

    val opacity by animateFloatAsState(
        targetValue = if (phase >= 2) 0f else 1f,
        animationSpec = tween(700),
        label = "opacity"
    )

    val translateY by animateDpAsState(
        targetValue = if (phase >= 1) 0.dp else 20.dp,
        animationSpec = tween(600, delayMillis = 300),
        label = "translateY"
    )

    LaunchedEffect(Unit) {
        delay(100)
        phase = 1
        delay(1400)
        phase = 2
        delay(700)
        onAnimationFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (dark) Color(0xFF1A1A1D) else Color(0xFFFFF8E1))
            .graphicsLayer(alpha = opacity),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .scale(scale)
                    .graphicsLayer(rotationZ = rotation)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFFFBBC04), Color(0xFFF9A825), Color(0xFFFF8F00))
                        ),
                        shape = RoundedCornerShape(22.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "QuickNotes",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = if (dark) Color(0xFFFFF8E1) else Color(0xFF5D4037),
                modifier = Modifier.graphicsLayer(translationY = translateY.value)
            )

            Text(
                text = "CAPTURE EVERYTHING",
                fontSize = 13.sp,
                letterSpacing = 2.sp,
                color = if (dark) Color(0x80FFF8E1) else Color(0x805D4037),
                modifier = Modifier.graphicsLayer(translationY = translateY.value)
            )
        }
    }
}
