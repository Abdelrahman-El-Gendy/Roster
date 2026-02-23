package com.gndy.peoplelog.presentation.input

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gndy.peoplelog.domain.Gender
import com.gndy.peoplelog.presentation.components.RosterScreenContainer
import com.gndy.peoplelog.presentation.input.components.*
import com.gndy.peoplelog.ui.theme.*
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun InputScreen(
    viewModel: InputViewModel,
    onNavigateToDisplay: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is InputEffect.UserSaved -> {
                    snackbarHostState.showSnackbar(
                        message = "New team member onboarded successfully",
                        withDismissAction = true
                    )
                }
            }
        }
    }

    InputScreenContent(
        state = state,
        onFullNameChange = viewModel::onFullNameChange,
        onAgeChange = viewModel::onAgeChange,
        onJobTitleChange = viewModel::onJobTitleChange,
        onGenderSelect = viewModel::onGenderSelect,
        onSave = viewModel::onSave,
        onNavigateToDisplay = onNavigateToDisplay,
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun InputScreenContent(
    state: InputUiState,
    onFullNameChange: (String) -> Unit,
    onAgeChange: (String) -> Unit,
    onJobTitleChange: (String) -> Unit,
    onGenderSelect: (Gender) -> Unit,
    onSave: () -> Unit,
    onNavigateToDisplay: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val scrollState = rememberScrollState()

    RosterScreenContainer(
        snackbarHostState = snackbarHostState,
        bottomBar = {
            InputBottomBar(
                isSaving = state.isSaving,
                onNavigateToDisplay = onNavigateToDisplay,
                onSave = onSave
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .verticalScroll(scrollState)
        ) {
            InputHeader()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(28.dp)
            ) {
                ProductionTextField(
                    value = state.fullName,
                    onValueChange = onFullNameChange,
                    label = "Full Name",
                    icon = Icons.Default.Person,
                    error = state.fullNameError
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    ProductionTextField(
                        value = state.age,
                        onValueChange = onAgeChange,
                        label = "Age",
                        icon = Icons.Default.CalendarToday,
                        keyboardType = KeyboardType.Number,
                        error = state.ageError,
                        modifier = Modifier.weight(1f)
                    )
                    ProductionTextField(
                        value = state.jobTitle,
                        onValueChange = onJobTitleChange,
                        label = "Job Title",
                        icon = Icons.Default.Badge,
                        error = state.jobTitleError,
                        modifier = Modifier.weight(2f)
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "Member Gender",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Slate900
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Gender.entries.forEach { gender ->
                            GenderSelectionCard(
                                gender = gender,
                                isSelected = state.gender == gender,
                                onClick = { onGenderSelect(gender) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
                
                Spacer(Modifier.height(40.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InputScreenPreview() {
    PeopleLogTheme {
        InputScreenContent(
            state = InputUiState(
                fullName = "Abdelrahman G.",
                age = "25",
                jobTitle = "Android Developer",
                gender = Gender.Male
            ),
            onFullNameChange = {},
            onAgeChange = {},
            onJobTitleChange = {},
            onGenderSelect = {},
            onSave = {},
            onNavigateToDisplay = {},
            snackbarHostState = SnackbarHostState()
        )
    }
}