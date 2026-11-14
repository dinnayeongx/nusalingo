
package com.example.nusalingo.feature.lesson.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nusalingo.data.local.CharacterEntity
import com.example.nusalingo.feature.lesson.audio.playAudio
import com.example.nusalingo.feature.lesson.ui.LessonViewModel
import com.example.nusalingo.feature.lesson.ui.lessonDescription
import com.example.nusalingo.feature.lesson.ui.sectionNote
import com.example.nusalingo.ui.theme.JavaneseFont
import com.example.nusalingo.ui.theme.NunitoFont
import com.example.nusalingo.ui.theme.NusalingoColors

@Composable
fun LessonDetailRoute(
    lessonTitle: String,
    onBack: () -> Unit,
    viewModel: LessonViewModel = viewModel()
) {
    val context = LocalContext.current

    val characters by remember(lessonTitle) {
        viewModel.getCharacters(lessonTitle)
    }.collectAsState(initial = emptyList())

    LessonDetailScreen(
        title = lessonTitle,
        characters = characters,
        onBack = onBack,
        onPlayAudio = { character ->
            playAudio(context, character.audioName)
        },
        onCompleteLesson = { learnedCount ->
            viewModel.completeLesson(lessonTitle, learnedCount)

            Toast.makeText(
                context,
                "Progres tersimpan",
                Toast.LENGTH_SHORT
            ).show()
        }
    )
}


@Composable
fun LessonDetailScreen(
    title: String,
    characters: List<CharacterEntity>,
    onBack: () -> Unit,
    onPlayAudio: (CharacterEntity) -> Unit,
    onCompleteLesson: (Int) -> Unit
) {
    var viewedIds by remember(title) {
        mutableStateOf(setOf<Int>())
    }

    var selectedCharacter by remember {
        mutableStateOf<CharacterEntity?>(null)
    }

    val isAvailable = characters.isNotEmpty()

    val viewedCount = viewedIds.size.coerceAtMost(characters.size)

    val canComplete = isAvailable &&
            characters.all { it.id in viewedIds }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Text(
                text = "‹",
                modifier = Modifier
                    .clickable(onClick = onBack)
                    .padding(end = 18.dp),
                fontSize = 44.sp,
                color = Color.Black,
                fontFamily = NunitoFont
            )

            Text(
                text = title,
                fontFamily = NunitoFont,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
                color = NusalingoColors.TextPrimary
            )
        }

        if (isAvailable) {
            Text(
                text = lessonDescription(title),
                fontFamily = NunitoFont,
                fontSize = 16.sp,
                lineHeight = 25.sp,
                color = NusalingoColors.TextSecondary,
                modifier = Modifier.padding(
                    start = 8.dp,
                    bottom = 24.dp
                )
            )
            Text(
                text = "Karakter dipelajari: $viewedCount/${characters.size}",
                fontFamily = NunitoFont,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = NusalingoColors.TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))
            ProgressBar(
                fraction = viewedCount.toFloat() / characters.size.toFloat()
            )

            Spacer(modifier = Modifier.height(20.dp))
            val sections = characters.groupBy {
                it.section
            }

            sections.forEach { (sectionTitle, sectionCharacters) ->
                ScriptChartSection(
                    title = sectionTitle,
                    characters = sectionCharacters,
                    viewedIds = viewedIds,
                    onCharacterClick = { character ->

                        selectedCharacter = character
                        viewedIds = viewedIds + character.id
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            Button(
                onClick = {
                    onCompleteLesson(viewedCount)
                },
                enabled = canComplete,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NusalingoColors.ActionSecondary
                )
            ) {
                Text(
                    text = if (canComplete) {
                        "Selesaikan Materi"
                    } else {
                        "Pelajari semua karakter dulu"
                    },
                    fontFamily = NunitoFont,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

        } else {
            UnavailableCard(title)
        }
    }

    selectedCharacter?.let { current ->

        AlertDialog(
            onDismissRequest = {
                selectedCharacter = null
            },

            confirmButton = {
                TextButton(
                    onClick = {
                        selectedCharacter = null
                    }
                ) {
                    Text(
                        text = "Tutup",
                        fontFamily = NunitoFont
                    )
                }
            },

            title = {
                Text(
                    text = current.latin,
                    fontFamily = NunitoFont,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 28.sp
                )
            },

            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        text = current.glyph,
                        fontFamily = JavaneseFont,
                        fontSize = 80.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            onPlayAudio(current)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NusalingoColors.ActionPrimary
                        )
                    ) {
                        Text(
                            text = "Dengarkan Pelafalan",
                            fontFamily = NunitoFont
                        )
                    }
                }
            }
        )
    }
}
@Composable
fun ScriptChartSection(
    title: String,
    characters: List<CharacterEntity>,
    viewedIds: Set<Int>,
    onCharacterClick: (CharacterEntity) -> Unit
) {
    val columns = if (
        characters.any {
            it.latin.length > 4 || it.glyph.length > 2
        }
    ) {
        3
    } else {
        5
    }
    val note = sectionNote(title)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(NusalingoColors.ChartGreen)
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                color = Color.White,
                fontFamily = NunitoFont,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp
            )
        }
        if (note.isNotEmpty()) {
            Text(
                text = note,
                fontFamily = NunitoFont,
                fontSize = 14.sp,
                lineHeight = 21.sp,
                color = NusalingoColors.TextSecondary,
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 12.dp
                )
            )
        }
        Column(
            modifier = Modifier.padding(
                horizontal = 8.dp,
                vertical = 12.dp
            )
        ) {
            characters.chunked(columns).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    row.forEach { character ->

                        val isViewed = character.id in viewedIds

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(2.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isViewed) {
                                        NusalingoColors.ChartGreen.copy(
                                            alpha = 0.2f
                                        )
                                    } else {
                                        Color.Transparent
                                    }
                                )
                                .clickable {
                                    onCharacterClick(character)
                                }
                                .padding(vertical = 8.dp),

                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = character.glyph,
                                fontFamily = JavaneseFont,
                                fontSize = 28.sp,
                                color = Color.Black,
                                maxLines = 1
                            )
                            Text(
                                text = character.latin,
                                fontFamily = NunitoFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF456B3C),
                                maxLines = 1
                            )
                        }
                    }
                    repeat(columns - row.size) {
                        Spacer(
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun ProgressBar(fraction: Float) {
    val progress = fraction.coerceIn(0f, 1f)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Color.LightGray)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth(progress)
                .fillMaxHeight()
                .background(NusalingoColors.ChartGreen)
        )
    }
}
@Composable
fun UnavailableCard(title: String) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(
                horizontal = 24.dp,
                vertical = 36.dp
            ),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Materi Belum Tersedia",
            fontFamily = NunitoFont,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = NusalingoColors.TextPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Materi $title sedang dalam pengembangan. Silakan pilih materi lain yang sudah tersedia.",
            fontFamily = NunitoFont,
            fontSize = 15.sp,
            lineHeight = 23.sp,
            color = NusalingoColors.TextSecondary
        )
    }
}
