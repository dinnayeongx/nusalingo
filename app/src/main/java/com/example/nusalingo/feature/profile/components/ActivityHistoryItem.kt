package com.example.nusalingo.feature.profile.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nusalingo.data.model.ActivityHistory
import com.example.nusalingo.data.model.ActivityType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ActivityHistoryItem(
    activity: ActivityHistory,
    modifier: Modifier = Modifier
) {
    val (icon, typeLabel) = activity.type.display()
    val dateText = SimpleDateFormat("d MMM yyyy", Locale("id", "ID"))
        .format(Date(activity.timestamp))

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = ProfileColors.Cream,
        border = BorderStroke(1.5.dp, ProfileColors.Navy)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = typeLabel,
                    tint = ProfileColors.Navy,
                    modifier = Modifier.size(26.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text = activity.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ProfileColors.Navy
                    )
                    val scoreText = activity.score?.let { " • Skor $it" }.orEmpty()
                    val learnedText = activity.learnedCount?.let { " • $it aksara" }.orEmpty()
                    Text(
                        text = "$typeLabel$scoreText$learnedText • $dateText",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
            activity.xpEarned?.let { xp ->
                Text(
                    text = "+$xp XP",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = ProfileColors.Orange
                )
            }
        }
    }
}

private fun ActivityType.display(): Pair<ImageVector, String> = when (this) {
    ActivityType.LESSON -> Icons.AutoMirrored.Filled.MenuBook to "Lesson"
    ActivityType.QUIZ -> Icons.Filled.Quiz to "Quiz"
    ActivityType.PRACTICE -> Icons.Filled.Create to "Practice"
}
