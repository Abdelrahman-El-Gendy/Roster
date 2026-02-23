package com.gndy.peoplelog.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RosterScreenContainer(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: (@Composable () -> Unit)? = null,
    snackbarHostState: SnackbarHostState? = null,
    containerColor: Color = MaterialTheme.colorScheme.background,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = topBar,
        bottomBar = bottomBar ?: {},
        snackbarHost = { 
            snackbarHostState?.let { 
                SnackbarHost(
                    hostState = it,
                    modifier = Modifier.padding(bottom = if (bottomBar == null) 90.dp else 0.dp)
                ) 
            } 
        },
        containerColor = containerColor,
    ) { padding ->
        // If we don't have a local bottom bar, we add manual space for the global FloatingBottomBar
        val bottomSpace = if (bottomBar != null) 0.dp else 100.dp
        
        Box(modifier = Modifier.padding(padding)) {
            content(PaddingValues(bottom = bottomSpace))
        }
    }
}
