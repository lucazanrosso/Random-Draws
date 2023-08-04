package com.lucazanrosso.randomdraws.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.lucazanrosso.randomdraws.RandomDrawsApplication
import com.lucazanrosso.randomdraws.data.Item
import com.lucazanrosso.randomdraws.data.ItemDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GroupDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val dao: ItemDao,
) : ViewModel() {

    private val groupName: String = checkNotNull(savedStateHandle[GroupDetailsDestination.itemIdArg])

    val uiState: StateFlow<GroupDetailsUiState> =
        dao.getGroupDetails(groupName)
            .filterNotNull()
            .map {
                GroupDetailsUiState(it)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = GroupDetailsUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                GroupDetailsViewModel(
                    createSavedStateHandle(),
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RandomDrawsApplication).dao
                )
            }
        }
    }

}

data class GroupDetailsUiState(val itemList: List<Item> = listOf())