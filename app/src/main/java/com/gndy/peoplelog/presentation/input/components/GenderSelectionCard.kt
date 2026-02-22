package com.gndy.peoplelog.presentation.input.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Transgender
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gndy.peoplelog.domain.Gender
import com.gndy.peoplelog.ui.theme.*

@Composable
fun GenderSelectionCard(
    gender: Gender,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(if (isSelected) 1.05f else 1f, label = "scale")
    
    Surface(
        onClick = onClick,
        modifier = modifier
            .scale(scale)
            .height(80.dp),
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) Indigo50 else Color.Transparent,
        border = if (isSelected) 
            androidx.compose.foundation.BorderStroke(2.dp, Indigo500) 
        else 
            androidx.compose.foundation.BorderStroke(1.dp, Slate200),
       tonalElevation = if (isSelected) 4.dp else 0.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = when(gender) {
                    Gender.Male -> Icons.Default.Male
                    Gender.Female -> Icons.Default.Female
                    else -> Icons.Default.Transgender
                },
                contentDescription = null,
                tint = if (isSelected) Indigo600 else Slate400,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = gender.name,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Black,
                color = if (isSelected) Indigo600 else Slate600
            )
        }
    }
}
