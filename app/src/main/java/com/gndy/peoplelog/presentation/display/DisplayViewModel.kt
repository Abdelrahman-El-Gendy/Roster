package com.gndy.peoplelog.presentation.display

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gndy.peoplelog.domain.GetAllUsersUseCase
import com.gndy.peoplelog.domain.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

enum class SortType {
    Name, Date, JobTitle, Age, Gender
}

@HiltViewModel
class DisplayViewModel @Inject constructor(
    getAllUsersUseCase: GetAllUsersUseCase,
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _sortType = MutableStateFlow(SortType.Date)
    val sortType = _sortType.asStateFlow()

    val state: StateFlow<DisplayUiState> = combine(
        getAllUsersUseCase(),
        _searchQuery,
        _sortType
    ) { users, query, sort ->
        val filtered = users.filter {
            it.fullName.contains(query, ignoreCase = true) ||
            it.jobTitle.contains(query, ignoreCase = true)
        }

        val sorted = when (sort) {
            SortType.Name -> filtered.sortedBy { it.fullName }
            SortType.Date -> filtered.sortedByDescending { it.createdAt }
            SortType.JobTitle -> filtered.sortedBy { it.jobTitle }
            SortType.Age -> filtered.sortedBy { it.age }
            SortType.Gender -> filtered.sortedBy { it.gender.name }
        }

        if (sorted.isEmpty() && query.isEmpty()) DisplayUiState.Empty
        else DisplayUiState.Success(sorted)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DisplayUiState.Loading)

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onSortTypeChange(sort: SortType) {
        _sortType.value = sort
    }
}
