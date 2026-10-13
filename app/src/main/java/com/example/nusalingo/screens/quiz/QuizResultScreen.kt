package com.example.nusalingo.screens.quiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nusalingo.R
import com.example.nusalingo.components.PrimaryButton
import com.example.nusalingo.components.SecondaryButton
import com.example.nusalingo.ui.theme.NusalingoColors

@Composable
fun QuizResultScreen(
    xpEarned: Int,
    correctAnswers: Int,
    totalQuestions: Int,
    onContinue: () -> Unit,
    onHome: () -> Unit
) {
    val accuracy = if (totalQuestions > 0) {
        correctAnswers * 100 / totalQuestions
    } else {
        0
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NusalingoColors.Background)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.mascot),
            contentDescription = "Mascot Nusalingo",
            modifier = Modifier.size(220.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(Modifier.height(14.dp))

        Text(
            text = "Misi Selesai!",
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            color = NusalingoColors.TextPrimary
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Satu langkah lagi makin jago! Yuk, lanjut taklukkan tantangan aksara berikutnya",
            fontSize = 15.sp,
            color = NusalingoColors.TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(20.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = NusalingoColors.ActionPrimary
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "⚡ +$xpEarned XP",
                    color = NusalingoColors.TextInverse,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Total sekarang: $xpEarned XP",
                    color = NusalingoColors.TextInverse,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ResultStat(
                value = "$correctAnswers / $totalQuestions",
                label = "Jawaban benar",
                modifier = Modifier.weight(1f)
            )

            ResultStat(
                value = "$accuracy%",
                label = "Nilai kuis",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(70.dp))

        PrimaryButton(
            text = "Lanjutkan Belajar",
            onClick = onContinue
        )
    }
}

@Composable
private fun ResultStat(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = NusalingoColors.SurfacePrimary,
        border = BorderStroke(width = 1.dp, color = NusalingoColors.InputBorder)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = NusalingoColors.TextPrimary
            )

            Text(
                text = label,
                fontSize = 13.sp,
                color = NusalingoColors.TextSecondary
            )
        }
    }
}