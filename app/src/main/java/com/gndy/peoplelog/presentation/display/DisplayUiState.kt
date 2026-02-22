package com.gndy.peoplelog.presentation.display

import com.gndy.peoplelog.domain.User

sealed interface DisplayUiState {
    data object Loading : DisplayUiState
    data object Empty : DisplayUiState
    data class Success(val users: List<User>) : DisplayUiState
}
