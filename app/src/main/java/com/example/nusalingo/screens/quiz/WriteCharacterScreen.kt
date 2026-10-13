package com.example.nusalingo.screens.quiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nusalingo.components.PrimaryButton
import com.example.nusalingo.components.quiz.QuizTopBar
import com.example.nusalingo.components.quiz.WritingCanvas
import com.example.nusalingo.ui.theme.NusalingoColors

@Composable
fun WriteCharacterScreen(
    onExit: () -> Unit,
    onFinish: (correctAnswers: Int, totalQuestions: Int, xpEarned: Int) -> Unit
) {
    val characters = listOf("ꦲ", "ꦤ", "ꦕ", "ꦫ", "ꦮ")
    val readings = listOf("Ha", "Na", "Ca", "Ra", "Wa")

    val strokes = remember {
        mutableStateListOf<List<Offset>>()
    }

    var questionIndex by remember {
        mutableIntStateOf(0)
    }

    val currentCharacter = characters[questionIndex]
    val currentReading = readings[questionIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NusalingoColors.Background)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            QuizTopBar(
                progress = (questionIndex + 1).toFloat() / characters.size,
                xp = questionIndex * 10,
                onExit = onExit
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Tuliskan Aksara",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NusalingoColors.TextPrimary
            )

            Spacer(Modifier.height(20.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFF3379DD),
                    modifier = Modifier.size(34.dp),
                    onClick = {
                        // Audio dapat ditambahkan nanti
                    }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("🔊", fontSize = 16.sp)
                    }
                }

                Column(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = currentReading,
                        fontSize = 14.sp,
                        color = NusalingoColors.TextSecondary
                    )

                    Spacer(Modifier.height(0.dp))

                    Text(
                        text = currentCharacter,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = NusalingoColors.TextPrimary
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.TopCenter
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .border(
                            width = 1.dp,
                            color = NusalingoColors.InputBorder,
                            shape = RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    WritingCanvas(
                        modifier = Modifier.fillMaxSize(),
                        strokes = strokes,
                        onStrokeAdded = { stroke ->
                            strokes.add(stroke)
                        },
                        guideCharacter = currentCharacter
                    )

                    Text(
                        text = "Area menulis",
                        fontSize = 12.sp,
                        color = NusalingoColors.TextSecondary.copy(alpha = 0.7f),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 8.dp)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        if (strokes.isNotEmpty()) {
                            strokes.removeAt(strokes.lastIndex)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NusalingoColors.SurfacePrimary,
                        contentColor = NusalingoColors.TextPrimary
                    ),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(width = 1.dp, color = NusalingoColors.InputBorder)
                ) {
                    Text("↶ Urungkan", fontSize = 16.sp)
                }

                Button(
                    onClick = {
                        strokes.clear()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NusalingoColors.InputBackground,
                        contentColor = NusalingoColors.TextPrimary
                    ),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(width = 1.dp, color = NusalingoColors.InputBorder)
                ) {
                    Text("⟲ Ulangi", fontSize = 16.sp)
                }
            }
        }

        Spacer(Modifier.height(80.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(NusalingoColors.Background)
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
                .padding(bottom = 12.dp)
        ) {
            PrimaryButton(
                text = if (questionIndex < characters.lastIndex) {
                    "CEK"
                } else {
                    "Lihat Hasil"
                },
                onClick = {
                    if (questionIndex < characters.lastIndex) {
                        questionIndex++
                        strokes.clear()
                    } else {
                        onFinish(
                            0,
                            characters.size,
                            characters.size * 10
                        )
                    }
                }
            )
        }
    }
}