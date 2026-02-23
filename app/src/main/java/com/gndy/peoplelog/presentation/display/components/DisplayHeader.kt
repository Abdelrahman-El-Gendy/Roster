package com.gndy.peoplelog.presentation.display.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gndy.peoplelog.ui.theme.Indigo500
import com.gndy.peoplelog.ui.theme.Indigo700

@Composable
fun DisplayHeader(
    count: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp)
            .background(
                Brush.verticalGradient(
                    listOf(Indigo700, Indigo500)
                )
            )
            .padding(top = 60.dp, start = 24.dp, end = 24.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Column {
            Surface(
                color = Color.White.copy(alpha = 0.3f),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(
                    "TOTAL MEMBERS",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }
            Text(
                "The Roster",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 48.sp,
                    letterSpacing = (-1.5).sp
                ),
                color = Color.White
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "$count professionals are currently part\nof your organized team log.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.9f),
                lineHeight = 24.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
