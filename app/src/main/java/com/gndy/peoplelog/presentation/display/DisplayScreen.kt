package com.gndy.peoplelog.presentation.display

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gndy.peoplelog.domain.Gender
import com.gndy.peoplelog.domain.User
import com.gndy.peoplelog.presentation.components.RosterScreenContainer
import com.gndy.peoplelog.presentation.display.components.*
import com.gndy.peoplelog.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun DisplayScreen(
    viewModel: DisplayViewModel,
    onBack: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val sortType by viewModel.sortType.collectAsStateWithLifecycle()

    DisplayScreenContent(
        state = state,
        searchQuery = searchQuery,
        sortType = sortType,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onSortTypeChange = viewModel::onSortTypeChange,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayScreenContent(
    state: DisplayUiState,
    searchQuery: String,
    sortType: SortType,
    onSearchQueryChange: (String) -> Unit,
    onSortTypeChange: (SortType) -> Unit,
    onBack: () -> Unit
) {
    var isSearchActive by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    var showSortMenu by remember { mutableStateOf(false) }

    RosterScreenContainer(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            if (isSearchActive) {
                SearchTopBar(
                    query = searchQuery,
                    onQueryChange = onSearchQueryChange,
                    onClose = {
                        isSearchActive = false
                        onSearchQueryChange("")
                    },
                    focusRequester = focusRequester
                )
            } else {
                LargeTopAppBar(
                    title = {
                        Column {
                            Text(
                                "The Roster",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = (-1).sp
                                )
                            )
                            if (state is DisplayUiState.Success) {
                                Text(
                                    "${state.users.size} team members total",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = { isSearchActive = true }) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }
                        Box {
                            IconButton(onClick = { showSortMenu = true }) {
                                Icon(Icons.Default.FilterList, contentDescription = "Filter")
                            }
                            DropdownMenu(
                                expanded = showSortMenu,
                                onDismissRequest = { showSortMenu = false },
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.surface)
                                    .border(1.dp, Slate200, RoundedCornerShape(12.dp))
                            ) {
                                Text(
                                    "Sort By",
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                SortType.entries.forEach { type ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                type.name.replace("([a-z])([A-Z])".toRegex(), "$1 $2"),
                                                fontWeight = if (sortType == type) FontWeight.Black else FontWeight.Medium
                                            )
                                        },
                                        onClick = {
                                            onSortTypeChange(type)
                                            showSortMenu = false
                                        },
                                        trailingIcon = {
                                            if (sortType == type) {
                                                Icon(Icons.Default.Check, contentDescription = null, tint = Indigo600)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    },
                    scrollBehavior = scrollBehavior,
                    colors = TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        scrolledContainerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onBackground
                    )
                )
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            if (!isSearchActive) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Sorted by ", style = MaterialTheme.typography.bodySmall, color = Slate400)
                    Text(
                        sortType.name.replace("([a-z])([A-Z])".toRegex(), "$1 $2"),
                        style = MaterialTheme.typography.bodySmall,
                        color = Indigo600,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            AnimatedContent(
                targetState = state,
                transitionSpec = {
                    fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(300))
                },
                label = "state_transition"
            ) { currentState ->
                when (currentState) {
                    is DisplayUiState.Loading -> LoadingView()
                    is DisplayUiState.Empty -> EmptyView(
                        isSearching = searchQuery.isNotEmpty(),
                        onAddClick = onBack,
                        onClearSearch = { onSearchQueryChange("") }
                    )
                    is DisplayUiState.Success -> UserListView(currentState.users)
                }
            }
        }
    }

    LaunchedEffect(isSearchActive) {
        if (isSearchActive) {
            delay(100)
            focusRequester.requestFocus()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DisplayScreenPreview() {
    PeopleLogTheme {
        DisplayScreenContent(
            state = DisplayUiState.Success(
                users = listOf(
                    User(1, "John Doe", 30, "Android Developer", Gender.Male, System.currentTimeMillis()),
                    User(2, "Jane Doe", 28, "iOS Developer", Gender.Female, System.currentTimeMillis())
                )
            ),
            searchQuery = "",
            sortType = SortType.Date,
            onSearchQueryChange = {},
            onSortTypeChange = {},
            onBack = {}
        )
    }
}
