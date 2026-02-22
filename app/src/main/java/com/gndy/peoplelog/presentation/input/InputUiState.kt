package com.gndy.peoplelog.presentation.input

import com.gndy.peoplelog.domain.Gender

data class InputUiState(
    val fullName: String = "",
    val age: String = "",
    val jobTitle: String = "",
    val gender: Gender = Gender.Male,
    val fullNameError: String? = null,
    val ageError: String? = null,
    val jobTitleError: String? = null,
    val isSaving: Boolean = false,
)

sealed interface InputEffect {
    data object UserSaved : InputEffect
}
