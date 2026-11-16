package com.example.nusalingo.feature.quiz.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nusalingo.ui.components.PrimaryButton
import com.example.nusalingo.ui.components.SecondaryButton
import com.example.nusalingo.ui.theme.NunitoFont
import com.example.nusalingo.ui.theme.NusalingoColors

@Composable
fun QuizAnswersScreen(
    wrongAnswers: List<QuizReviewAnswer>,
    onRetry: () -> Unit,
    onHome: () -> Unit,
    homeButtonText: String = "Kembali ke Latihan",
    retryEnabled: Boolean = wrongAnswers.isNotEmpty()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NusalingoColors.Background)
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Jawaban yang Perlu Dipelajari",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = NusalingoColors.TextPrimary
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Berikut soal yang salah beserta kunci jawabannya.",
            fontSize = 15.sp,
            color = NusalingoColors.TextSecondary
        )

        Spacer(Modifier.height(20.dp))

        if (wrongAnswers.isEmpty()) {
            Text(
                text = "Semua jawabanmu benar. Tidak ada soal yang perlu ditinjau.",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .wrapContentHeight(Alignment.CenterVertically),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = NusalingoColors.TextPrimary,
                textAlign = TextAlign.Center
            )
        } else {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                wrongAnswers.forEach { item ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = NusalingoColors.SurfacePrimary,
                        border = BorderStroke(1.dp, NusalingoColors.InputBorder)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Soal ${item.questionNumber}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = NusalingoColors.TextSecondary
                            )

                            Spacer(Modifier.height(8.dp))

                            Text(
                                text = item.prompt,
                                fontFamily = NunitoFont,
                                fontSize = 24.sp,
                                lineHeight = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = NusalingoColors.TextPrimary
                            )

                            Spacer(Modifier.height(8.dp))

                            Text(
                                text = "Kunci jawaban:",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = NusalingoColors.TextSecondary
                            )

                            Spacer(Modifier.height(4.dp))

                            Text(
                                text = item.correctAnswer,
                                fontSize = 24.sp,
                                lineHeight = 30.sp,
                                fontWeight = FontWeight.Medium,
                                color = NusalingoColors.TextPrimary
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        PrimaryButton(
            text = "Ulangi Latihan",
            onClick = onRetry,
            enabled = retryEnabled
        )

        Spacer(Modifier.height(10.dp))

        SecondaryButton(
            text = homeButtonText,
            onClick = onHome
        )
    }
}
