package com.example.nusalingo.feature.quiz.screen

import android.media.AudioAttributes
import android.media.SoundPool
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalDensity
import com.example.nusalingo.ui.components.quiz.MatchingPairCard
import com.example.nusalingo.ui.components.quiz.QuizScreenTitle
import com.example.nusalingo.ui.components.quiz.QuizTopBar
import com.example.nusalingo.ui.theme.NusalingoColors
import kotlinx.coroutines.delay
import kotlin.random.Random

private const val TOTAL_QUESTIONS = 5
private const val PAIRS_PER_QUESTION = 5
private const val XP_PER_PAIR = 2
private const val MATCH_FLASH_DURATION_MS = 850
private const val WRONG_FLASH_DURATION_MS = 650

private data class MatchingPair(
    val id: Int,
    val glyph: String,
    val reading: String,
    val audioName: String
)

private data class ConfettiPiece(
    val x: Float,
    val drift: Float,
    val depth: Float,
    val rotation: Float,
    val width: Float,
    val height: Float,
    val color: Color
)

private val matchingPairs = listOf(
    MatchingPair(0, "ꦲ", "Ha", "aksara_ha"),
    MatchingPair(1, "ꦤ", "Na", "aksara_na"),
    MatchingPair(2, "ꦕ", "Ca", "aksara_ca"),
    MatchingPair(3, "ꦫ", "Ra", "aksara_ra"),
    MatchingPair(4, "ꦏ", "Ka", "aksara_ka"),
    MatchingPair(5, "ꦢ", "Da", "aksara_da"),
    MatchingPair(6, "ꦠ", "Ta", "aksara_ta"),
    MatchingPair(7, "ꦱ", "Sa", "aksara_sa"),
    MatchingPair(8, "ꦮ", "Wa", "aksara_wa"),
    MatchingPair(9, "ꦭ", "La", "aksara_la"),
    MatchingPair(10, "ꦥ", "Pa", "aksara_pa"),
    MatchingPair(11, "ꦝ", "Dha", "aksara_dha"),
    MatchingPair(12, "ꦗ", "Ja", "aksara_ja"),
    MatchingPair(13, "ꦪ", "Ya", "aksara_ya"),
    MatchingPair(14, "ꦚ", "Nya", "aksara_nya"),
    MatchingPair(15, "ꦩ", "Ma", "aksara_ma"),
    MatchingPair(16, "ꦒ", "Ga", "aksara_ga"),
    MatchingPair(17, "ꦧ", "Ba", "aksara_ba"),
    MatchingPair(18, "ꦛ", "Tha", "aksara_tha"),
    MatchingPair(19, "ꦔ", "Nga", "aksara_nga"),
    MatchingPair(20, "◌ꦶ", "i", "sandhangan_wulu"),
    MatchingPair(21, "◌ꦸ", "u", "sandhangan_suku"),
    MatchingPair(22, "◌ꦼ", "ê", "sandhangan_pepet"),
    MatchingPair(23, "◌ꦺ", "e", "sandhangan_taling"),
    MatchingPair(24, "◌ꦺꦴ", "o", "sandhangan_taling_tarung")
)

@Composable
fun MatchingPairsScreen(
    onExit: () -> Unit,
    onFinish: (
        correctAnswers: Int,
        totalQuestions: Int,
        xpEarned: Int
    ) -> Unit,
    levelMode: Boolean = false,
    onLevelQuestionFinished: ((Boolean) -> Unit)? = null,
    progressOverride: Float? = null,
    xpOverride: Int? = null
) {
    val context = LocalContext.current
    val soundPool = remember(context) {
        SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .build()
    }
    val soundSampleIds = remember(soundPool) { mutableMapOf<String, Int>() }
    val loadedSoundIds = remember(soundPool) { mutableStateOf(setOf<Int>()) }
    var pendingSoundIds by remember(soundPool) { mutableStateOf(emptyList<Int>()) }
    val pairs = remember { matchingPairs.shuffled() }
    val questionPages = remember(pairs, levelMode) {
        val pages = pairs.chunked(PAIRS_PER_QUESTION)
        if (levelMode) pages.take(1) else pages
    }

    var questionIndex by remember { mutableIntStateOf(0) }
    var selectedCharacterIndex by remember { mutableIntStateOf(-1) }
    var selectedReadingIndex by remember { mutableIntStateOf(-1) }
    var matchedPairIds by remember { mutableStateOf(setOf<Int>()) }
    var pairsInWrongMatches by remember { mutableStateOf(setOf<Int>()) }
    var flashingPairIds by remember { mutableStateOf(setOf<Int>()) }
    var wrongCharacterIndex by remember { mutableIntStateOf(-1) }
    var wrongReadingIndex by remember { mutableIntStateOf(-1) }
    var wrongFlashId by remember { mutableIntStateOf(0) }

    val currentQuestion = questionPages[questionIndex]
    val shuffledReadings = remember(questionIndex) {
        currentQuestion.map { it.reading }.shuffled()
    }
    val matchedInQuestion = currentQuestion.count { it.id in matchedPairIds }
    val completedQuestions = questionIndex + if (matchedInQuestion == PAIRS_PER_QUESTION) 1 else 0
    val totalQuestionPages = if (levelMode) 1 else TOTAL_QUESTIONS
    val progress = completedQuestions.toFloat() / totalQuestionPages
    val xpEarned = matchedPairIds.count { it !in pairsInWrongMatches } * XP_PER_PAIR

    DisposableEffect(soundPool, context) {
        soundPool.setOnLoadCompleteListener { player, sampleId, status ->
            if (status == 0) {
                loadedSoundIds.value = loadedSoundIds.value + sampleId
                if (sampleId in pendingSoundIds) {
                    player.play(sampleId, 1f, 1f, 1, 0, 1f)
                    pendingSoundIds = pendingSoundIds - sampleId
                }
            } else {
                pendingSoundIds = pendingSoundIds - sampleId
                Toast.makeText(context, "Audio aksara gagal dimuat.", Toast.LENGTH_SHORT).show()
            }
        }

        matchingPairs.forEach { pair ->
            val resourceId = context.resources.getIdentifier(
                pair.audioName,
                "raw",
                context.packageName
            )
            if (resourceId == 0) {
                Toast.makeText(context, "Audio ${pair.audioName} belum tersedia.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                soundSampleIds[pair.audioName] = soundPool.load(context, resourceId, 1)
            }
        }

        onDispose {
            soundPool.setOnLoadCompleteListener(null)
            soundPool.release()
        }
    }

    LaunchedEffect(flashingPairIds) {
        if (flashingPairIds.isNotEmpty()) {
            val pairsToClear = flashingPairIds
            delay(MATCH_FLASH_DURATION_MS.toLong())
            flashingPairIds = flashingPairIds - pairsToClear
        }
    }

    LaunchedEffect(wrongFlashId) {
        if (wrongFlashId > 0) {
            val activeFlashId = wrongFlashId
            delay(WRONG_FLASH_DURATION_MS.toLong())
            if (wrongFlashId == activeFlashId) {
                wrongCharacterIndex = -1
                wrongReadingIndex = -1
                selectedCharacterIndex = -1
                selectedReadingIndex = -1
            }
        }
    }

    LaunchedEffect(questionIndex, matchedInQuestion) {
        if (matchedInQuestion == PAIRS_PER_QUESTION) {
            delay(MATCH_FLASH_DURATION_MS.toLong())
            if (levelMode) {
                val perfect = currentQuestion.none { it.id in pairsInWrongMatches }
                onLevelQuestionFinished?.invoke(perfect)
            } else if (questionIndex == TOTAL_QUESTIONS - 1) {
                onFinish(TOTAL_QUESTIONS, TOTAL_QUESTIONS, xpEarned)
            } else {
                questionIndex++
                selectedCharacterIndex = -1
                selectedReadingIndex = -1
            }
        }
    }

    fun playPairAudio(pair: MatchingPair) {
        val sampleId = soundSampleIds[pair.audioName] ?: return
        if (sampleId in loadedSoundIds.value) {
            val streamId = soundPool.play(sampleId, 1f, 1f, 1, 0, 1f)
            if (streamId == 0) {
                Toast.makeText(context, "Audio aksara tidak dapat diputar.", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            pendingSoundIds = (pendingSoundIds + sampleId).distinct()
        }
    }

    fun checkSelectedPair() {
        if (selectedCharacterIndex !in currentQuestion.indices ||
            selectedReadingIndex !in shuffledReadings.indices
        ) {
            return
        }

        val selectedPair = currentQuestion[selectedCharacterIndex]
        val selectedReadingPair = currentQuestion.first {
            it.reading == shuffledReadings[selectedReadingIndex]
        }

        if (selectedPair.id == selectedReadingPair.id) {
            playPairAudio(selectedPair)
            matchedPairIds = matchedPairIds + selectedPair.id
            flashingPairIds = flashingPairIds + selectedPair.id
            selectedCharacterIndex = -1
            selectedReadingIndex = -1
        } else {
            pairsInWrongMatches = pairsInWrongMatches +
                    selectedPair.id +
                    selectedReadingPair.id
            wrongCharacterIndex = selectedCharacterIndex
            wrongReadingIndex = selectedReadingIndex
            wrongFlashId++
        }
    }

    fun selectCharacter(index: Int) {
        if (wrongCharacterIndex >= 0) return
        if (currentQuestion[index].id in matchedPairIds) return

        selectedCharacterIndex = if (selectedCharacterIndex == index) -1 else index
        if (selectedCharacterIndex >= 0 && selectedReadingIndex >= 0) {
            checkSelectedPair()
        }
    }

    fun selectReading(index: Int) {
        if (wrongCharacterIndex >= 0) return
        val pair = currentQuestion.first { it.reading == shuffledReadings[index] }
        if (pair.id in matchedPairIds) return

        selectedReadingIndex = if (selectedReadingIndex == index) -1 else index
        if (selectedReadingIndex >= 0 && selectedCharacterIndex >= 0) {
            checkSelectedPair()
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
                .padding(bottom = 16.dp)
                .navigationBarsPadding()
        ) {
            QuizTopBar(
                progress = progressOverride ?: progress,
                xp = xpOverride ?: xpEarned,
                onExit = onExit
            )

            Spacer(Modifier.height(18.dp))

            QuizScreenTitle(text = "Pasangkan Aksara")
            Spacer(Modifier.height(18.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    currentQuestion.forEachIndexed { index, pair ->
                        val matched = pair.id in matchedPairIds
                        val flashing = pair.id in flashingPairIds
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(105.dp)
                                .clip(RoundedCornerShape(14.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            MatchingPairCard(
                                text = pair.glyph,
                                isCharacter = true,
                                selected = selectedCharacterIndex == index,
                                matched = matched,
                                matchedFlash = flashing,
                                incorrect = wrongCharacterIndex == index,
                                enabled = wrongCharacterIndex < 0 && !matched,
                                onClick = { selectCharacter(index) },
                                modifier = Modifier.fillMaxSize()
                            )
                            if (flashing) {
                                ConfettiBurst(key = pair.id)
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    shuffledReadings.forEachIndexed { index, reading ->
                        val pair = currentQuestion.first { it.reading == reading }
                        val matched = pair.id in matchedPairIds
                        val flashing = pair.id in flashingPairIds
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(105.dp)
                                .clip(RoundedCornerShape(14.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            MatchingPairCard(
                                text = reading,
                                selected = selectedReadingIndex == index,
                                matched = matched,
                                matchedFlash = flashing,
                                incorrect = wrongReadingIndex == index,
                                enabled = wrongCharacterIndex < 0 && !matched,
                                onClick = { selectReading(index) },
                                modifier = Modifier.fillMaxSize()
                            )
                            if (flashing) {
                                ConfettiBurst(key = pair.id)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ConfettiBurst(key: Int) {
    val density = LocalDensity.current.density
    val progress = remember(key) { Animatable(0f) }
    val colors = listOf(
        Color(0xFF16AD61),
        NusalingoColors.ActionPrimary,
        Color(0xFFFFC857),
        Color(0xFFE8795A),
        Color(0xFF8A71C8)
    )
    val pieces = remember(key, density) {
        val random = Random(key)
        List(24) {
            ConfettiPiece(
                x = 0.12f + random.nextFloat() * 0.76f,
                drift = (random.nextFloat() - 0.5f) * 55f * density,
                depth = 0.3f + random.nextFloat() * 0.7f,
                rotation = random.nextFloat() * 360f,
                width = (3f + random.nextFloat() * 3f) * density,
                height = (4f + random.nextFloat() * 4f) * density,
                color = colors[random.nextInt(colors.size)]
            )
        }
    }

    LaunchedEffect(key) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(MATCH_FLASH_DURATION_MS)
        )
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        pieces.forEach { piece ->
            val x = (size.width * piece.x + piece.drift * progress.value)
                .coerceIn(0f, size.width)
            val y = size.height * progress.value * piece.depth
            val alpha = (1f - progress.value).coerceIn(0f, 1f)

            rotate(
                degrees = piece.rotation + 360f * progress.value,
                pivot = Offset(x, y)
            ) {
                drawRect(
                    color = piece.color.copy(alpha = alpha),
                    topLeft = Offset(x, y),
                    size = Size(piece.width, piece.height)
                )
            }
        }
    }
}
