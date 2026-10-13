package com.example.nusalingo.screens.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nusalingo.components.PrimaryButton
import com.example.nusalingo.components.quiz.MatchingPairCard
import com.example.nusalingo.components.quiz.QuizTopBar
import com.example.nusalingo.ui.theme.NusalingoColors

private data class MatchingPair(
    val character: String,
    val reading: String
)

@Composable
fun MatchingPairsScreen(
    onExit: () -> Unit,
    onFinish: (
        correctAnswers: Int,
        totalQuestions: Int,
        xpEarned: Int
    ) -> Unit
) {
    val pairs = remember {
        listOf(
            MatchingPair("ꦲ", "Ha"),
            MatchingPair("ꦤ", "Na"),
            MatchingPair("ꦕ", "Ca"),
            MatchingPair("ꦫ", "Ra"),
            MatchingPair("ꦮ", "Wa")
        )
    }

    val shuffledReadings = remember {
        pairs.map { it.reading }.shuffled()
    }

    var selectedCharacter by remember {
        mutableStateOf<Int?>(null)
    }

    var selectedReading by remember {
        mutableStateOf<Int?>(null)
    }

    var matchedPairs by remember {
        mutableStateOf(setOf<Int>())
    }

    var mistakes by remember {
        mutableIntStateOf(0)
    }

    var correctAnswers by remember {
        mutableIntStateOf(0)
    }

    var checked by remember {
        mutableStateOf(false)
    }

    var lastPairCorrect by remember {
        mutableStateOf(false)
    }

    var feedback by remember {
        mutableStateOf("")
    }

    val finished = matchedPairs.size == pairs.size

    fun checkPair() {
        val characterIndex = selectedCharacter ?: return
        val readingIndex = selectedReading ?: return

        lastPairCorrect =
            pairs[characterIndex].reading == shuffledReadings[readingIndex]

        checked = true

        if (lastPairCorrect) {
            matchedPairs = matchedPairs + characterIndex
            correctAnswers++
            feedback = "Kamu Berhasil!"
        } else {
            mistakes++
            feedback = "Belum tepat!"
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NusalingoColors.Background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .padding(bottom = 110.dp)
        ) {
            QuizTopBar(
                progress = matchedPairs.size.toFloat() / pairs.size,
                xp = correctAnswers * 10,
                onExit = onExit
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Cari pasangan aksara yang cocok",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NusalingoColors.TextPrimary
            )

            Spacer(Modifier.height(40.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    pairs.forEachIndexed { index, pair ->
                        MatchingPairCard(
                            text = pair.character,
                            isCharacter = true,
                            selected = selectedCharacter == index,
                            matched = index in matchedPairs,
                            onClick = {
                                if (!checked && index !in matchedPairs) {
                                    selectedCharacter = index
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    shuffledReadings.forEachIndexed { index, reading ->
                        val characterIndex = pairs.indexOfFirst {
                            it.reading == reading
                        }

                        MatchingPairCard(
                            text = reading,
                            selected = selectedReading == index,
                            matched = characterIndex in matchedPairs,
                            onClick = {
                                if (!checked &&
                                    characterIndex !in matchedPairs
                                ) {
                                    selectedReading = index
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(Modifier.weight(1f))
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(NusalingoColors.Background)
                .navigationBarsPadding()
        ) {
            if (checked) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (lastPairCorrect) {
                                NusalingoColors.GreenSecondary
                            } else {
                                Color(0xFFF4B6B6)
                            }
                        )
                        .padding(
                            start = 20.dp,
                            end = 20.dp,
                            top = 16.dp,
                            bottom = 12.dp
                        )
                ) {
                    Text(
                        text = feedback,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = NusalingoColors.TextPrimary
                    )

                    Spacer(Modifier.height(12.dp))

                    PrimaryButton(
                        text = if (finished) {
                            "Lihat Hasil"
                        } else {
                            "Selanjutnya"
                        },
                        containerColor = if (lastPairCorrect) {
                            Color(0xFF16AD61)
                        } else {
                            NusalingoColors.ActionPrimary
                        },
                        onClick = {
                            if (finished) {
                                onFinish(
                                    correctAnswers,
                                    pairs.size,
                                    correctAnswers * 10
                                )
                            } else {
                                selectedCharacter = null
                                selectedReading = null
                                checked = false
                                feedback = ""
                            }
                        }
                    )
                }
            } else {
                PrimaryButton(
                    text = "CEK",
                    onClick = {
                        if (selectedCharacter != null &&
                            selectedReading != null
                        ) {
                            checkPair()
                        }
                    },
                    enabled = selectedCharacter != null &&
                            selectedReading != null,
                    modifier = Modifier.padding(
                        horizontal = 20.dp,
                        vertical = 12.dp
                    )
                )
            }
        }
    }
}