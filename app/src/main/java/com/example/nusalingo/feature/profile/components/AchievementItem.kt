package com.example.nusalingo.feature.profile.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nusalingo.data.model.Achievement

@Composable
fun AchievementItem(
    achievement: Achievement,
    modifier: Modifier = Modifier
) {
    val unlocked = achievement.unlocked

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .alpha(if (unlocked) 1f else 0.75f),
        shape = RoundedCornerShape(14.dp),
        color = ProfileColors.Cream,
        border = BorderStroke(2.dp, if (unlocked) ProfileColors.Navy else ProfileColors.Divider)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (unlocked) Icons.Filled.EmojiEvents else Icons.Filled.Lock,
                contentDescription = null,
                tint = if (unlocked) ProfileColors.Orange else Color.Gray,
                modifier = Modifier.size(34.dp)
            )
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = achievement.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = ProfileColors.Navy
                )
                Text(
                    text = achievement.description,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                if (!unlocked && achievement.target > 0) {
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = {
                            (achievement.progress.toFloat() / achievement.target).coerceIn(0f, 1f)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp),
                        color = ProfileColors.Blue,
                        trackColor = ProfileColors.Divider
                    )
                    Text(
                        text = "${achievement.progress}/${achievement.target}",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}
