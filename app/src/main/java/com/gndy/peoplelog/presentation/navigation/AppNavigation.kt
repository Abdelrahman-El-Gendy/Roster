package com.gndy.peoplelog.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.gndy.peoplelog.presentation.display.DisplayScreen
import com.gndy.peoplelog.presentation.display.DisplayViewModel
import com.gndy.peoplelog.presentation.input.InputScreen
import com.gndy.peoplelog.presentation.input.InputViewModel

@Composable
fun AppNavigation() {
    val backStack = rememberNavBackStack(NavRoute.Input)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<NavRoute.Input> {
                val viewModel = hiltViewModel<InputViewModel>()
                InputScreen(
                    viewModel = viewModel,
                    onNavigateToDisplay = { backStack.add(NavRoute.Display) },
                )
            }

            entry<NavRoute.Display> {
                val viewModel = hiltViewModel<DisplayViewModel>()
                DisplayScreen(
                    viewModel = viewModel,
                    onBack = { backStack.removeLastOrNull() },
                )
            }
        },
    )
}
