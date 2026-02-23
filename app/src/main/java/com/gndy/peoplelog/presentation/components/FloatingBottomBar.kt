package com.gndy.peoplelog.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gndy.peoplelog.presentation.navigation.NavRoute
import com.gndy.peoplelog.ui.theme.*

@Composable
fun FloatingBottomBar(
    currentRoute: NavRoute,
    onRouteSelected: (NavRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .padding(horizontal = 48.dp, vertical = 24.dp)
            .height(72.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(36.dp),
        color = Color.White,
        shadowElevation = 12.dp,
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                label = "Roster",
                icon = Icons.Default.FormatListBulleted,
                isSelected = currentRoute is NavRoute.Display,
                onClick = { onRouteSelected(NavRoute.Display) },
                modifier = Modifier.weight(1f)
            )
            BottomNavItem(
                label = "Add",
                icon = Icons.Default.PersonAddAlt1,
                isSelected = currentRoute is NavRoute.Input,
                onClick = { onRouteSelected(NavRoute.Input) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else Slate500,
        label = "color"
    )
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Navy800 else Color.Transparent,
        label = "background"
    )

    Box(
        modifier = modifier
            .fillMaxHeight(0.85f)
            .clip(RoundedCornerShape(32.dp))
            .background(backgroundColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )
            if (isSelected) {
                Spacer(Modifier.width(8.dp))
                Text(
                    text = label,
                    color = contentColor,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}
