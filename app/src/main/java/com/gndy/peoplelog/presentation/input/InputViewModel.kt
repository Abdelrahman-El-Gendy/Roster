package com.gndy.peoplelog.presentation.input

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gndy.peoplelog.domain.Gender
import com.gndy.peoplelog.domain.InsertUserUseCase
import com.gndy.peoplelog.domain.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InputViewModel @Inject constructor(
    private val insertUserUseCase: InsertUserUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(InputUiState())
    val state: StateFlow<InputUiState> = _state.asStateFlow()

    private val _effects = Channel<InputEffect>()
    val effects = _effects.receiveAsFlow()

    fun onFullNameChange(value: String) {
        _state.update { it.copy(fullName = value, fullNameError = null) }
    }

    fun onAgeChange(value: String) {
        _state.update { it.copy(age = value, ageError = null) }
    }

    fun onJobTitleChange(value: String) {
        _state.update { it.copy(jobTitle = value, jobTitleError = null) }
    }

    fun onGenderSelect(gender: Gender) {
        _state.update { it.copy(gender = gender) }
    }

    fun onSave() {
        val current = _state.value
        var hasError = false

        var fullNameError: String? = null
        var ageError: String? = null
        var jobTitleError: String? = null

        if (current.fullName.isBlank()) {
            fullNameError = "Full name is required"
            hasError = true
        }

        val ageInt = current.age.toIntOrNull()
        if (current.age.isBlank()) {
            ageError = "Age is required"
            hasError = true
        } else if (ageInt == null || ageInt <= 0 || ageInt > 150) {
            ageError = "Enter a valid age (1–150)"
            hasError = true
        }

        if (current.jobTitle.isBlank()) {
            jobTitleError = "Job title is required"
            hasError = true
        }

        if (hasError) {
            _state.update {
                it.copy(
                    fullNameError = fullNameError,
                    ageError = ageError,
                    jobTitleError = jobTitleError,
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            insertUserUseCase(
                User(
                    fullName = current.fullName.trim(),
                    age = ageInt!!,
                    jobTitle = current.jobTitle.trim(),
                    gender = current.gender,
                ),
            )
            _state.update { InputUiState() } // reset form
            _effects.send(InputEffect.UserSaved)
        }
    }
}
