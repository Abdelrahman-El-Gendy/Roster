package com.gndy.peoplelog.presentation.input

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gndy.peoplelog.domain.Gender
import com.gndy.peoplelog.presentation.components.RosterScreenContainer
import com.gndy.peoplelog.presentation.input.components.*
import com.gndy.peoplelog.ui.theme.*
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun InputScreen(
    viewModel: InputViewModel
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
    snackbarHostState: SnackbarHostState
) {
    val scrollState = rememberScrollState()

    RosterScreenContainer(
        snackbarHostState = snackbarHostState,
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
                .verticalScroll(scrollState)
        ) {
            InputHeader()

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .offset(y = (-50).dp)
                    .shadow(
                        elevation = 20.dp,
                        shape = RoundedCornerShape(40.dp),
                        spotColor = Color.Black.copy(alpha = 0.2f)
                    ),
                shape = RoundedCornerShape(40.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    ProductionTextField(
                        value = state.fullName,
                        onValueChange = onFullNameChange,
                        label = "Full Name",
                        placeholder = "e.g. John Doe",
                        icon = Icons.Default.Person,
                        error = state.fullNameError
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(), 
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ProductionTextField(
                            value = state.age,
                            onValueChange = onAgeChange,
                            label = "Age",
                            placeholder = "25",
                            icon = Icons.Default.CalendarToday,
                            keyboardType = KeyboardType.Number,
                            error = state.ageError,
                            modifier = Modifier.weight(1f)
                        )
                        ProductionTextField(
                            value = state.jobTitle,
                            onValueChange = onJobTitleChange,
                            label = "Job Title",
                            placeholder = "Product Design",
                            icon = Icons.Default.Badge,
                            error = state.jobTitleError,
                            modifier = Modifier.weight(1.5f)
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            "MEMBER GENDER",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Black,
                            color = Slate500,
                            letterSpacing = 0.5.sp
                        )
                        
                        // Row 1: Male, Female
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            GenderSelectionCard(
                                gender = Gender.Male,
                                isSelected = state.gender == Gender.Male,
                                onClick = { onGenderSelect(Gender.Male) },
                                modifier = Modifier.weight(1f)
                            )
                            GenderSelectionCard(
                                gender = Gender.Female,
                                isSelected = state.gender == Gender.Female,
                                onClick = { onGenderSelect(Gender.Female) },
                                modifier = Modifier.weight(1.3f)
                            )
                        }
                        
                        // Row 2: Other
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            GenderSelectionCard(
                                gender = Gender.Other,
                                isSelected = state.gender == Gender.Other,
                                onClick = { onGenderSelect(Gender.Other) },
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(Modifier.weight(1.3f))
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    Button(
                        onClick = onSave,
                        enabled = !state.isSaving,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(68.dp),
                        shape = RoundedCornerShape(34.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Navy800,
                            disabledContainerColor = Navy800.copy(alpha = 0.6f)
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                    ) {
                        if (state.isSaving) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Icon(Icons.Default.PersonAdd, contentDescription = null)
                            Spacer(Modifier.width(12.dp))
                            Text(
                                "Onboard Member",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }
            
            // Padding for the bottom bar
            Spacer(Modifier.height(120.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InputScreenPreview() {
    PeopleLogTheme {
        InputScreenContent(
            state = InputUiState(
                fullName = "",
                age = "",
                jobTitle = "",
                gender = Gender.Female,
                isSaving = false
            ),
            onFullNameChange = {},
            onAgeChange = {},
            onJobTitleChange = {},
            onGenderSelect = {},
            onSave = {},
            snackbarHostState = SnackbarHostState()
        )
    }
}