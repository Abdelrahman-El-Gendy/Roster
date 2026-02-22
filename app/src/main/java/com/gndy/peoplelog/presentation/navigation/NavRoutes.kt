package com.gndy.peoplelog.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface NavRoute : NavKey {
    @Serializable
    data object Input : NavRoute

    @Serializable
    data object Display : NavRoute
}
