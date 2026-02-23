package com.gndy.peoplelog.presentation.input.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Female
import androidx.compose.material.icons.outlined.Male
import androidx.compose.material.icons.outlined.Transgender
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gndy.peoplelog.domain.Gender
import com.gndy.peoplelog.ui.theme.*

@Composable
fun GenderSelectionCard(
    gender: Gender,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(52.dp),
        shape = RoundedCornerShape(26.dp),
        color = if (isSelected) Navy800 else Color.White,
        border = if (isSelected) null else BorderStroke(1.dp, Slate200),
        shadowElevation = if (isSelected) 4.dp else 0.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = when(gender) {
                    Gender.Male -> Icons.Outlined.Male
                    Gender.Female -> Icons.Outlined.Female
                    else -> Icons.Outlined.Transgender
                },
                contentDescription = null,
                tint = if (isSelected) Color.White else Slate700,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = gender.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else Slate700,
                fontSize = 15.sp
            )
        }
    }
}
