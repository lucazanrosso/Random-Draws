package com.lucazanrosso.randomdraws.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.lucazanrosso.randomdraws.RandomDrawsApplication
import com.lucazanrosso.randomdraws.data.ItemDao
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.map
import androidx.lifecycle.viewModelScope
import com.lucazanrosso.randomdraws.data.Group
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

class GroupsViewModel (
    private val dao: ItemDao,
) : ViewModel() {

    fun deleteGroup (groupName: String) { viewModelScope.launch {
            dao.deleteGroup(groupName)
        }
    }

    val homeUiState: StateFlow<HomeUiState> =
        dao.getGroups().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                GroupsViewModel(
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RandomDrawsApplication).dao
                )
            }
        }
    }
}

data class HomeUiState(val group: List<Group> = listOf()) {}
