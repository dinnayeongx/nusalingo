package com.example.nusalingo.feature.auth.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nusalingo.R
import com.example.nusalingo.ui.theme.NunitoFont
import com.example.nusalingo.ui.theme.NusalingoColors
import kotlinx.coroutines.delay

@Composable
fun LandingRoute(onContinue: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2_400)
        onContinue()
    }

    LandingScreen()
}

@Composable
fun LandingScreen() {
    val wingMotion = rememberInfiniteTransition(label = "mascot wing flap")
    val wingPose by wingMotion.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(460, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "mascot wing pose",
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NusalingoColors.Background)
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(
                    if (wingPose < 0.5f) R.drawable.mascot_jump else R.drawable.mascot
                ),
                contentDescription = "Maskot burung NusaLingo mengepakkan sayap",
                modifier = Modifier.size(240.dp),
            )
            Spacer(Modifier.height(12.dp))
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(tween(500)) + slideInVertically(
                    initialOffsetY = { it / 3 },
                    animationSpec = tween(500),
                ),
            ) {
                Row {
                    Text(
                        text = "Nusa",
                        fontFamily = NunitoFont,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = NusalingoColors.LogoNusa,
                    )
                    Text(
                        text = "Lingo",
                        fontFamily = NunitoFont,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = NusalingoColors.LogoLingo,
                    )
                }
            }
        }
    }
}
