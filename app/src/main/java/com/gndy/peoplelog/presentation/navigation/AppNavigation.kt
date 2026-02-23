package com.gndy.peoplelog.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.gndy.peoplelog.presentation.components.FloatingBottomBar
import com.gndy.peoplelog.presentation.display.DisplayScreen
import com.gndy.peoplelog.presentation.display.DisplayViewModel
import com.gndy.peoplelog.presentation.input.InputScreen
import com.gndy.peoplelog.presentation.input.InputViewModel

@Composable
fun AppNavigation() {
    val backStack = rememberNavBackStack(NavRoute.Display)

    val currentRoute = backStack.lastOrNull() as? NavRoute

    Box(modifier = Modifier.fillMaxSize()) {
        NavDisplay(
            backStack = backStack,
            onBack = { 
                if (backStack.lastOrNull() == NavRoute.Display) {
                    while (backStack.isNotEmpty()) backStack.removeLastOrNull()
                    backStack.add(NavRoute.Input)
                } else if (backStack.size > 1) {
                    backStack.removeLastOrNull()
                }
            },
            entryProvider = entryProvider {
                entry<NavRoute.Input> {
                    val viewModel = hiltViewModel<InputViewModel>()
                    InputScreen(
                        viewModel = viewModel
                    )
                }

                entry<NavRoute.Display> {
                    val viewModel = hiltViewModel<DisplayViewModel>()
                    DisplayScreen(
                        viewModel = viewModel,
                        onBack = { 
                            // Explicitly navigate to InputScreen as requested
                            while (backStack.isNotEmpty()) backStack.removeLastOrNull()
                            backStack.add(NavRoute.Input)
                        },
                    )
                }
            },
        )

        currentRoute?.let { route ->
            FloatingBottomBar(
                currentRoute = route,
                onRouteSelected = { targetRoute ->
                    if (route != targetRoute) {
                        while (backStack.isNotEmpty()) {
                            backStack.removeLastOrNull()
                        }
                        backStack.add(targetRoute)
                    }
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}
