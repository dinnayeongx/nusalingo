package com.example.nusalingo.feature.auth.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nusalingo.R
import com.example.nusalingo.feature.auth.ui.OnboardingEvent
import com.example.nusalingo.feature.auth.ui.OnboardingUiState
import com.example.nusalingo.feature.auth.ui.OnboardingViewModel
import com.example.nusalingo.ui.components.AuthScaffold
import com.example.nusalingo.ui.components.PrimaryButton
import com.example.nusalingo.ui.theme.NunitoFont
import com.example.nusalingo.ui.theme.NusalingoColors
import kotlin.math.PI
import kotlin.math.sin

private val scripts = listOf("Aksara Jawa", "Aksara Sunda", "Aksara Bali")
private val experienceOptions = listOf(
    "Aku baru pertama kali belajar",
    "Aku tahu beberapa bentuk dasar hurufnya",
    "Aku bisa membaca kata-kata sederhana",
    "Aku bisa membaca dan menulis kalimat dasar",
    "Aku sudah lancar membaca dan menulis",
)
private val goalOptions = listOf(
    "Hanya untuk bersenang-senang",
    "Mendukung pelajaran sekolah",
    "Mendukung pekerjaan / karier",
    "Mengenal budaya & leluhur",
    "Mengisi waktu luang",
    "Lainnya",
)

private val questionBubbleShape = GenericShape { size, _ ->
    val tailWidth = size.width * 0.05f
    val tailTop = size.height * 0.22f
    val tailPoint = size.height * 0.34f
    val tailBottom = size.height * 0.46f

    moveTo(tailWidth, 0f)
    lineTo(size.width, 0f)
    lineTo(size.width, size.height)
    lineTo(tailWidth, size.height)
    lineTo(tailWidth, tailBottom)
    lineTo(0f, tailPoint)
    lineTo(tailWidth, tailTop)
    close()
}

@Composable
fun OnboardingRoute(onCompleted: () -> Unit) {
    val viewModel: OnboardingViewModel = viewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                OnboardingEvent.Completed -> onCompleted()
            }
        }
    }

    BackHandler(enabled = state.step == 0 || state.isLoading) {
        if (state.step > 0) viewModel.back()
    }

    OnboardingScreen(
        state = state,
        onBack = viewModel::back,
        onSelectScript = viewModel::selectScript,
        onSelectExperience = viewModel::selectExperience,
        onToggleGoal = viewModel::toggleGoal,
        onContinue = viewModel::continueOnboarding,
    )
}

@Composable
fun OnboardingScreen(
    state: OnboardingUiState,
    onBack: () -> Unit,
    onSelectScript: (String) -> Unit,
    onSelectExperience: (String) -> Unit,
    onToggleGoal: (String) -> Unit,
    onContinue: () -> Unit,
) {
    AuthScaffold(
        bottomBar = {
            if (state.errorMessage != null) {
                Text(
                    text = state.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp,
                )
            }
            PrimaryButton(
                text = when (state.step) {
                    2 -> "Simpan pilihanku"
                    3 -> "Mulai"
                    else -> "Lanjutkan"
                },
                onClick = onContinue,
                isLoading = state.isLoading,
                enabled = !state.isLoading,
            )
        },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (state.step > 0 && state.step < 3) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(NusalingoColors.ActionPrimary)
                        .clickable(onClick = onBack),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali",
                        modifier = Modifier.size(24.dp),
                        tint = NusalingoColors.TextInverse,
                    )
                }
                Spacer(Modifier.size(10.dp))
            }
            Text(
                text = "Nusa",
                fontFamily = NunitoFont,
                fontSize = 23.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NusalingoColors.LogoNusa,
            )
            Text(
                text = "Lingo",
                fontFamily = NunitoFont,
                fontSize = 23.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NusalingoColors.LogoLingo,
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = if (state.step < 3) "${state.step + 1} dari 3" else "Siap!",
                color = NusalingoColors.TextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        if (state.step < 3) {
            LinearProgressIndicator(
                progress = { (state.step + 1) / 3f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(8.dp)),
                color = NusalingoColors.ActionPrimary,
                trackColor = NusalingoColors.InputBorder,
            )
        } else {
            LinearProgressIndicator(
                progress = { 1f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(8.dp)),
                color = NusalingoColors.ActionPrimary,
                trackColor = NusalingoColors.InputBorder,
            )
        }
        Spacer(Modifier.height(18.dp))

        AnimatedContent(
            targetState = state.step,
            transitionSpec = {
                (slideInHorizontally { it / 8 } + fadeIn(tween(220))) togetherWith
                    (slideOutHorizontally { -it / 8 } + fadeOut(tween(160))) using
                    SizeTransform(clip = false)
            },
            label = "onboarding step",
        ) { step ->
            when (step) {
                0 -> OnboardingQuestion(
                    mascotRes = R.drawable.mascot_onboarding,
                    message = "Aksara apa yang ingin kamu pelajari?",
                    title = "Pilih Aksara Nusantara",
                ) {
                    scripts.forEach { option ->
                        SelectableOption(
                            text = option,
                            selected = state.selectedScript == option,
                            onClick = { onSelectScript(option) },
                        )
                        Spacer(Modifier.height(10.dp))
                    }
                }
                1 -> OnboardingQuestion(
                    mascotRes = R.drawable.mascot_onboarding,
                    message = "Seberapa paham kamu tentang aksara ini?",
                    title = "Pilih kemampuanmu",
                ) {
                    experienceOptions.forEach { option ->
                        SelectableOption(
                            text = option,
                            selected = state.experience == option,
                            onClick = { onSelectExperience(option) },
                        )
                        Spacer(Modifier.height(10.dp))
                    }
                }
                2 -> OnboardingQuestion(
                    mascotRes = R.drawable.mascot_onboarding,
                    message = "Kenapa kamu ingin belajar aksara Nusantara?",
                    title = "Sesuaikan tujuanmu",
                    subtitle = "Pilih satu atau beberapa tujuan.",
                ) {
                    goalOptions.forEach { option ->
                        SelectableOption(
                            text = option,
                            selected = option in state.learningGoals,
                            onClick = { onToggleGoal(option) },
                        )
                        Spacer(Modifier.height(10.dp))
                    }
                }
                else -> OnboardingComplete()
            }
        }
    }
}

@Composable
private fun OnboardingQuestion(
    mascotRes: Int,
    message: String,
    title: String,
    subtitle: String? = null,
    options: @Composable () -> Unit,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(mascotRes),
                contentDescription = "Maskot NusaLingo",
                modifier = Modifier.size(148.dp),
            )
            Spacer(Modifier.size(10.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(NusalingoColors.InputBackground, questionBubbleShape)
                    .border(
                        BorderStroke(2.dp, Color(0x998B5A2B)),
                        questionBubbleShape,
                    )
                    .padding(start = 22.dp, end = 14.dp, top = 14.dp, bottom = 14.dp),
            ) {
                Text(
                    text = message,
                    color = NusalingoColors.TextPrimary,
                    fontSize = 16.sp,
                    lineHeight = 21.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        Text(
            text = title,
            color = NusalingoColors.TextPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
        )
        if (subtitle != null) {
            Spacer(Modifier.height(3.dp))
            Text(subtitle, color = NusalingoColors.TextSecondary, fontSize = 13.sp)
        }
        Spacer(Modifier.height(12.dp))
        options()
    }
}

@Composable
private fun SelectableOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = if (selected) NusalingoColors.ActionPrimary else NusalingoColors.InputBorder,
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) {
                NusalingoColors.ActionPrimary.copy(alpha = 0.08f)
            } else {
                NusalingoColors.InputBackground
            },
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = text,
                modifier = Modifier.weight(1f),
                color = NusalingoColors.ActionPrimary,
                fontSize = 14.sp,
                lineHeight = 19.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            )
            Icon(
                imageVector = if (selected) Icons.Outlined.CheckCircle
                else Icons.Outlined.RadioButtonUnchecked,
                contentDescription = if (selected) "Dipilih" else "Belum dipilih",
                tint = if (selected) NusalingoColors.ActionPrimary else NusalingoColors.InputBorder,
            )
        }
    }
}

@Composable
private fun OnboardingComplete() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(356.dp),
            contentAlignment = Alignment.Center,
        ) {
            CelebrationConfetti(Modifier.fillMaxSize())
            Image(
                painter = painterResource(R.drawable.mascot_jump),
                contentDescription = "Maskot NusaLingo merayakan onboarding selesai",
                modifier = Modifier
                    .size(270.dp)
                    .offset(y = 40.dp),
            )
            Card(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(x = 100.dp, y = 108.dp),
                shape = questionBubbleShape,
                border = BorderStroke(2.dp, Color(0x998B5A2B)),
                colors = CardDefaults.cardColors(containerColor = NusalingoColors.InputBackground),
            ) {
                Text(
                    text = "selesai!",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 9.dp),
                    color = NusalingoColors.ActionPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                )
            }
        }
        OnboardingBenefitsCard()
    }
}

@Composable
private fun OnboardingBenefitsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, NusalingoColors.ActionSecondary.copy(alpha = 0.22f)),
        colors = CardDefaults.cardColors(containerColor = NusalingoColors.InputBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp),
        ) {
            AffirmationRow(
                title = "Mahir Membaca & Menulis Aksara",
                subtitle = "Latihan tulis tangan dan pelafalan tanpa beban",
            )
            BenefitDivider()
            AffirmationRow(
                title = "Bangun Kebiasaan Belajar",
                subtitle = "Pengingat harian, tantangan seru, dan sistem XP",
            )
            BenefitDivider()
            AffirmationRow(
                title = "Kuasai Berbagai Aksara Nusantara",
                subtitle = "Mulai dari Jawa, Sunda, Bali, hingga Lontara",
            )
        }
    }
}

@Composable
private fun AffirmationRow(
    title: String,
    subtitle: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(NusalingoColors.ActionSecondary.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = NusalingoColors.ActionSecondary,
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = title,
                color = NusalingoColors.ActionPrimary,
                fontSize = 13.sp,
                lineHeight = 17.sp,
                fontWeight = FontWeight.ExtraBold,
            )
            Text(
                text = subtitle,
                color = NusalingoColors.TextSecondary,
                fontSize = 11.sp,
                lineHeight = 15.sp,
            )
        }
    }
}

@Composable
private fun BenefitDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(NusalingoColors.InputBorder),
    )
}

@Composable
private fun CelebrationConfetti(modifier: Modifier = Modifier) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 2_200, easing = LinearEasing),
        )
    }

    Canvas(modifier) {
        val colors = listOf(
            NusalingoColors.LogoLingo,
            NusalingoColors.ChartGreen,
            NusalingoColors.CardYellow,
            NusalingoColors.ActionPrimary,
        )
        val alpha = 1f - progress.value

        repeat(24) { index ->
            val sideOffset = if (index % 2 == 0) 0.04f else 0.78f
            val x = (sideOffset + (index * 17 % 20) / 100f) * size.width +
                sin(progress.value * 2f * PI.toFloat() + index.toFloat()) * 7.dp.toPx()
            val start = (index * 31 % 100) / 100f
            val y = ((start + progress.value * 0.75f) % 1f) * size.height
            val width = (4 + index % 3).dp.toPx()
            val height = width * 1.6f

            drawRect(
                color = colors[index % colors.size].copy(alpha = alpha),
                topLeft = Offset(x, y),
                size = Size(width, height),
            )
        }
    }
}
