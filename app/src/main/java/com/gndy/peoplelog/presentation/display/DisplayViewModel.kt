package com.gndy.peoplelog.presentation.display

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gndy.peoplelog.domain.GetAllUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DisplayViewModel @Inject constructor(
    getAllUsersUseCase: GetAllUsersUseCase,
) : ViewModel() {

    val state: StateFlow<DisplayUiState> = getAllUsersUseCase()
        .map { users ->
            if (users.isEmpty()) DisplayUiState.Empty
            else DisplayUiState.Success(users)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DisplayUiState.Loading)
}
