package com.gndy.peoplelog.presentation.input

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Transgender
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gndy.peoplelog.domain.Gender
import com.gndy.peoplelog.ui.theme.*
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
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

@OptIn(ExperimentalMaterial3Api::class)
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

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            Surface(
                tonalElevation = 8.dp,
                shadowElevation = 16.dp,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(20.dp)
                        .navigationBarsPadding(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onNavigateToDisplay,
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.5.dp)
                    ) {
                        Icon(Icons.Default.Group, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("The Roster", fontWeight = FontWeight.ExtraBold)
                    }

                    Button(
                        onClick = onSave,
                        enabled = !state.isSaving,
                        modifier = Modifier
                            .weight(1.5f)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                        )
                    ) {
                        if (state.isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 3.dp
                            )
                        } else {
                            Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Onboard Member", fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .verticalScroll(scrollState)
        ) {
            // Elegant Modern Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(
                        Brush.linearGradient(
                            listOf(Indigo700, Indigo500)
                        )
                    )
                    .padding(horizontal = 24.dp, vertical = 40.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Column {
                    Surface(
                        color = Color.White.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        Text(
                            "NEW ENTRY",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Black
                        )
                    }
                    Text(
                        "Onboarding",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 38.sp,
                            letterSpacing = (-1.5).sp
                        ),
                        color = Color.White
                    )
                    Text(
                        "Fill in the details below to add a new member to the professional roster.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.8f),
                        lineHeight = 22.sp
                    )
                }
            }

            // High Fidelity Form
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
                    Box(modifier = Modifier.weight(1f)) {
                        ProductionTextField(
                            value = state.age,
                            onValueChange = onAgeChange,
                            label = "Age",
                            icon = Icons.Default.CalendarToday,
                            keyboardType = KeyboardType.Number,
                            error = state.ageError
                        )
                    }
                    Box(modifier = Modifier.weight(2f)) {
                        ProductionTextField(
                            value = state.jobTitle,
                            onValueChange = onJobTitleChange,
                            label = "Job Title",
                            icon = Icons.Default.Badge,
                            error = state.jobTitleError
                        )
                    }
                }

                // Interactive Gender Selector
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

@Composable
private fun ProductionTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    error: String? = null
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.ExtraBold,
            color = if (error != null) Rose500 else Slate700
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            leadingIcon = { 
                Icon(
                    icon, 
                    contentDescription = null, 
                    tint = if (error != null) Rose500 else Indigo500,
                    modifier = Modifier.size(20.dp)
                ) 
            },
            singleLine = true,
            isError = error != null,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Slate200,
                focusedBorderColor = Indigo500,
                errorBorderColor = Rose500,
                focusedContainerColor = Slate50.copy(alpha = 0.5f),
                unfocusedContainerColor = Slate50.copy(alpha = 0.5f),
                unfocusedLeadingIconColor = Slate400,
                focusedLeadingIconColor = Indigo500,
                errorLeadingIconColor = Rose500
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
        )
        AnimatedVisibility(
            visible = error != null,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 4.dp)) {
                Icon(Icons.Default.Error, contentDescription = null, tint = Rose500, modifier = Modifier.size(14.dp))
                Spacer(Modifier.width(4.dp))
                Text(
                    text = error ?: "",
                    color = Rose500,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun GenderSelectionCard(
    gender: Gender,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(if (isSelected) 1.05f else 1f, label = "scale")
    
    Surface(
        onClick = onClick,
        modifier = modifier
            .scale(scale)
            .height(80.dp),
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) Indigo50 else Color.Transparent,
        border = if (isSelected) 
            androidx.compose.foundation.BorderStroke(2.dp, Indigo500) 
        else 
            androidx.compose.foundation.BorderStroke(1.dp, Slate200),
       tonalElevation = if (isSelected) 4.dp else 0.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = when(gender) {
                    Gender.Male -> Icons.Default.Male
                    Gender.Female -> Icons.Default.Female
                    else -> Icons.Default.Transgender
                },
                contentDescription = null,
                tint = if (isSelected) Indigo600 else Slate400,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = gender.name,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Black,
                color = if (isSelected) Indigo600 else Slate600
            )
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