package com.gndy.peoplelog.presentation.input.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
fun InputHeader(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp)
            .background(
                Brush.linearGradient(
                    listOf(Indigo700, Indigo500)
                )
            )
            .padding(horizontal = 24.dp, vertical = 40.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        Column {
            Surface(
                color = Color.White.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(
                    "NEW ENTRY",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Black
                )
            }
            Text(
                "Onboarding",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 38.sp,
                    letterSpacing = (-1.5).sp
                ),
                color = Color.White
            )
            Text(
                "Fill in the details below to add a new member to the professional roster.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.8f),
                lineHeight = 22.sp
            )
        }
    }
}
