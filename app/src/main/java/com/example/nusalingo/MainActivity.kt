//package com.example.nusalingo
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import com.example.nusalingo.ui.theme.NusalingoTheme
//import androidx.compose.runtime.SideEffect
//import androidx.compose.ui.graphics.toArgb
//import androidx.core.view.WindowCompat
//import com.example.nusalingo.screens.quiz.QuizFlowScreen
//import com.example.nusalingo.screens.quiz.QuizType
//import com.example.nusalingo.ui.theme.NusalingoColors
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        setContent {
////            NusalingoTheme {
////                val backgroundColor = NusalingoColors.Background
////
////                SideEffect {
////                    window.statusBarColor = backgroundColor.toArgb()
////                    window.navigationBarColor = backgroundColor.toArgb()
////
////                    WindowCompat.getInsetsController(
////                        window,
////                        window.decorView
////                    ).apply {
////                        isAppearanceLightStatusBars = true
////                        isAppearanceLightNavigationBars = true
////                    }
////                }
////
////                Surface(
////                    modifier = Modifier.fillMaxSize(),
////                    color = androidx.compose.material3.MaterialTheme.colorScheme.background
////                ) {
////                    Box(
////                        contentAlignment = Alignment.Center,
////                        modifier = Modifier.fillMaxSize()
////                    ) {
////                        Text(
////                            text = "Nusa",
////                            color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
////                            fontSize = 36.sp,
////                            fontWeight = FontWeight.ExtraBold
////                        )
////                    }
////                }
////            }
//
//            NusalingoTheme {
//                val backgroundColor = NusalingoColors.Background
//
//                SideEffect {
//                    window.statusBarColor = backgroundColor.toArgb()
//                    window.navigationBarColor = backgroundColor.toArgb()
//
//                    WindowCompat.getInsetsController(
//                        window,
//                        window.decorView
//                    ).apply {
//                        isAppearanceLightStatusBars = true
//                        isAppearanceLightNavigationBars = true
//                    }
//                }
//
//                QuizFlowScreen(
//                    quizType = QuizType.AUDIO_QUIZ
//                )
//            }
//
//        }
//    }
//}

package com.example.nusalingo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import com.example.nusalingo.screens.NusaLingoApp
import com.example.nusalingo.ui.theme.NusalingoColors
import com.example.nusalingo.ui.theme.NusalingoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)

        setContent {
            NusalingoTheme {
                val backgroundColor = NusalingoColors.Background
                SideEffect {
                    window.statusBarColor = backgroundColor.toArgb()
                    window.navigationBarColor = backgroundColor.toArgb()
                    WindowCompat.getInsetsController(window, window.decorView).apply {
                        isAppearanceLightStatusBars = true
                        isAppearanceLightNavigationBars = true
                    }
                }
                NusaLingoApp()
            }
        }
    }
}
