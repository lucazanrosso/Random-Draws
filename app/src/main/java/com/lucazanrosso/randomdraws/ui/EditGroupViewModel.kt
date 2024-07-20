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

class EditGroupViewModel(
    savedStateHandle: SavedStateHandle,
    private val dao: ItemDao,
) : ViewModel() {

    private val groupName: String = checkNotNull(savedStateHandle[EditGroupDestination.itemIdArg])
    var newGroupName by mutableStateOf(groupName)
    var list = mutableStateListOf<ItemDetails>()
    private var itemsToDelete = mutableStateListOf<ItemDetails>()
    private var progressiveIdForKeys = mutableStateOf(0)
    var isValid by mutableStateOf(true)

    init {
        viewModelScope.launch {
            list = dao.getGroupDetails(groupName)
                .filterNotNull()
                .first()
                .mapIndexed{ index, item -> ItemDetails(item.id, index, item.group, item.name, item.extracted) }
                .toMutableStateList()
            progressiveIdForKeys.value = list.size
        }
    }

    /*init {
//        viewModelScope.launch {
        list = dao.getGroupDetails(groupName)
//                .filterNotNull()
//                .first()
            .mapIndexed{ index, item -> ItemDetails(item.id, index, item.group, item.name, item.extracted) }
            .toMutableStateList()
        progressiveIdForKeys.value = list.size
//        }
    }*/

    private fun validateInput(): Boolean {
        if (newGroupName.isEmpty()) return false
        if (list.isEmpty()) return false
        list.forEach {
            if (it.name.isEmpty()) return false
        }
        return true
    }

    fun updateGroupName(groupName: String) {
        newGroupName = groupName
        isValid = validateInput()
    }

    fun addItemToList(index: Int) {
        list.add(index, ItemDetails(index = progressiveIdForKeys.value, group = groupName, name = ""))
        progressiveIdForKeys.value++
        isValid = validateInput()
    }

    fun updateItem(index: Int, name: String) {
        list[index].name = name
        isValid = validateInput()
    }

    fun deleteItem(item: ItemDetails) {
        list.remove(item)
        itemsToDelete.add(item)
        isValid = validateInput()
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
            list.forEach {
                it.group = newGroupName
                dao.upsert(it.toItem())
            }
        }
    }

    companion object {
//        private const val TIMEOUT_MILLIS = 5_000L
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                EditGroupViewModel(
                    createSavedStateHandle(),
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RandomDrawsApplication).dao
                )
            }
        }
    }
}
