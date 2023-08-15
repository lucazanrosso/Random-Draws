package com.lucazanrosso.randomdraws.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.lucazanrosso.randomdraws.RandomDrawsApplication
import com.lucazanrosso.randomdraws.data.ItemDao
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class GroupDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val dao: ItemDao,
) : ViewModel() {

    val groupName: String = checkNotNull(savedStateHandle[GroupDetailsDestination.itemIdArg])
    var newGroupName by mutableStateOf(groupName)
    var itemUiState = mutableStateListOf<ItemDetails>()
    var itemsToDelete = mutableStateListOf<ItemDetails>()
    var progressiveIdForKeys = mutableStateOf(0)
        private set

    init {
        viewModelScope.launch {
            itemUiState = dao.getGroupDetails(groupName)
                .filterNotNull()
                .first()
                .mapIndexed{ index, item -> ItemDetails(item.id, index, item.group, item.name) }
                .toMutableStateList()
            progressiveIdForKeys.value = itemUiState.size
        }
    }

    fun addItemToList(index: Int) {
        itemUiState.add(index, ItemDetails(index = progressiveIdForKeys.value, group = groupName, name = ""))
        progressiveIdForKeys.value++
    }

    fun updateItem(index: Int, name: String) {
        itemUiState[index].name = name
    }

    fun deleteItem(item: ItemDetails) {
        itemUiState.remove(item)
        itemsToDelete.add(item)
    }

    fun deleteItemsFromDb() {
        viewModelScope.launch {
            itemsToDelete.forEach {
                dao.delete(it.toItem())
            }
        }
    }

    fun upsertItems() {
        viewModelScope.launch {
            itemUiState.forEach {
                it.group = newGroupName
                dao.upsert(it.toItem())
            }
        }
    }

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
