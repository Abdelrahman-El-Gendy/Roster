package com.gndy.peoplelog.presentation.display

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gndy.peoplelog.domain.Gender
import com.gndy.peoplelog.domain.User
import com.gndy.peoplelog.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
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
    val focusManager = LocalFocusManager.current
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    var showSortMenu by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
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
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Active Filter Chip
            if (!isSearchActive) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Sorted by ",
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate400
                    )
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
                    is DisplayUiState.Success -> ListView(currentState.users)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchTopBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClose: () -> Unit,
    focusRequester: FocusRequester
) {
    TopAppBar(
        title = {
            TextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = { Text("Search by name or title...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = MaterialTheme.typography.bodyLarge,
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { /* Done via Flow */ })
            )
        },
        navigationIcon = {
            IconButton(onClick = onClose) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
    )
}

@Composable
private fun LoadingView() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(
                strokeWidth = 4.dp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                "Loading Roster...",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
private fun EmptyView(
    isSearching: Boolean,
    onAddClick: () -> Unit,
    onClearSearch: () -> Unit
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(40.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                modifier = Modifier.size(140.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (isSearching) Icons.Default.SearchOff else Icons.Default.Group,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(Modifier.height(32.dp))
            Text(
                if (isSearching) "No Matches Found" else "Your Roster is Silent",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
            Text(
                if (isSearching) "We couldn't find anyone matching your search. Try different keywords or clear the filter."
                else "Every great team starts with a single member. Add someone now to bring your roster to life.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )
            Spacer(Modifier.height(32.dp))
            if (isSearching) {
                OutlinedButton(
                    onClick = onClearSearch,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Clear Search")
                }
            } else {
                Button(
                    onClick = onAddClick,
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Add First Member")
                }
            }
        }
    }
}

@Composable
private fun ListView(users: List<User>) {
    LazyColumn(
        contentPadding = PaddingValues(top = 8.dp, bottom = 80.dp, start = 20.dp, end = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(users, key = { _, user -> user.id }) { index, user ->
            val scale = remember { Animatable(0.8f) }
            val alpha = remember { Animatable(0f) }

            LaunchedEffect(index) {
                launch {
                    delay(minOf(index * 60L, 300L))
                    scale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow))
                }
                launch {
                    delay(minOf(index * 60L, 300L))
                    alpha.animateTo(1f, tween(400))
                }
            }

            UserListItem(
                user = user,
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = scale.value
                        scaleY = scale.value
                        this.alpha = alpha.value
                    }
            )
        }
    }
}

@Composable
private fun UserListItem(user: User, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(28.dp),
                spotColor = Color.Black.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user.fullName.take(1).uppercase(),
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black
                )
            }

            Spacer(Modifier.width(20.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.fullName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Icon(
                        Icons.Default.Badge,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = user.jobTitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MetadataChip(
                        icon = Icons.Default.CalendarToday,
                        text = "${user.age} Years",
                        containerColor = Slate50,
                        contentColor = Slate600
                    )
                    MetadataChip(
                        icon = Icons.Default.Person,
                        text = user.gender.name,
                        containerColor = when(user.gender) {
                            Gender.Male -> Color(0xFFEFF6FF)
                            Gender.Female -> Color(0xFFFFF1F2)
                            else -> Color(0xFFF8FAFC)
                        },
                        contentColor = when(user.gender) {
                            Gender.Male -> Color(0xFF2563EB)
                            Gender.Female -> Color(0xFFE11D48)
                            else -> Color(0xFF64748B)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun MetadataChip(
    icon: ImageVector,
    text: String,
    containerColor: Color,
    contentColor: Color
) {
    Surface(
        color = containerColor,
        contentColor = contentColor,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(14.dp))
            Spacer(Modifier.width(6.dp))
            Text(
                text = text,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp
            )
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
                    User(
                        id = 1,
                        fullName = "John Doe",
                        jobTitle = "Android Developer",
                        age = 30,
                        gender = Gender.Male,
                        createdAt = System.currentTimeMillis()
                    ),
                    User(
                        id = 2,
                        fullName = "Jane Doe",
                        jobTitle = "iOS Developer",
                        age = 28,
                        gender = Gender.Female,
                        createdAt = System.currentTimeMillis()
                    )
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

@Preview(showBackground = true)
@Composable
fun DisplayScreenEmptyPreview() {
    PeopleLogTheme {
        DisplayScreenContent(
            state = DisplayUiState.Empty,
            searchQuery = "",
            sortType = SortType.Date,
            onSearchQueryChange = {},
            onSortTypeChange = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DisplayScreenLoadingPreview() {
    PeopleLogTheme {
        DisplayScreenContent(
            state = DisplayUiState.Loading,
            searchQuery = "",
            sortType = SortType.Date,
            onSearchQueryChange = {},
            onSortTypeChange = {},
            onBack = {}
        )
    }
}
