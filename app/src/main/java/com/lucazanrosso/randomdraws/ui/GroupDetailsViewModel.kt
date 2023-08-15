package com.lucazanrosso.randomdraws.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GroupDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val dao: ItemDao,
) : ViewModel() {

    val groupName: String = checkNotNull(savedStateHandle[GroupDetailsDestination.itemIdArg])
    var group = mutableStateOf(groupName)
    var list = mutableStateListOf<ItemDetails>()



    var uiState: StateFlow<GroupDetailsUiState> =
        dao.getGroupDetails(groupName)
            .filterNotNull()
            .map {
                GroupDetailsUiState(it)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = GroupDetailsUiState()
            )


//    var itemUiState = mutableStateListOf<ItemDetails>()
    var itemUiState = mutableStateListOf<ItemDetails>()
//        private set

    init {
        viewModelScope.launch {
            itemUiState = dao.getGroupDetails(groupName)
                .filterNotNull()
                .first()
                .mapIndexed{ index, item -> ItemDetails(item.id, index, item.group, item.name) }
                .toMutableStateList()
        }
    }
    /* devo creare una funzione che mi crei una nuova lista
    di itemDetails con un id progressivo come fatto per il NewGroup*/

//    fun Item.toItemUiState(): ItemUiState = ItemUiState(
//        itemDetails = this.toItemDetails()
//    )

//    class ItemUiStateFun (index: Int, item: Item) {
//        val id = index
//        var name = item.name
//        var group = item.group
//    }

//    fun List<Item>.toItemDetails(): List<ItemDetails> = List<ItemDetails>(
//
//        id = id,
//        name = name,
//        group = group
//    )

//    val snapshot = SnapshotStateList.fromList(itemUiState)

//    fun updateGroup() {
//        viewModelScope.launch {
//            list.forEach {
//                if (it.name.isNotEmpty())
//                    dao.insert(Item(group = group.value, name = it.name))
//            }
//        }
//    }

//    private var progressiveIdForKeys = mutableStateOf(3)

    fun updateItem(index: Int, name: String) {
        itemUiState[index].name = name
    }

    fun updateGroup(name: String) {
        viewModelScope.launch {
            dao.updateGroup(groupName, name)
        }
    }

//    fun removeItem(item: Item) {
//        viewModelScope.launch {
//            dao.delete(item)
//        }
//    }

    fun removeItem(index : Int) {
        itemUiState.removeAt(index)
    }

    fun upsertItems() {
        viewModelScope.launch {
            itemUiState.forEach {
                dao.upsert(it.toItem())
            }
        }
    }

    fun removeVoidItems() {
        viewModelScope.launch {
            dao.deleteVoidItems()
        }
    }

//    fun addItemToList() {
//        viewModelScope.launch {
//            dao.insert(Item(0, groupName, ""))
//        }
//    }

    fun addItemToList() {
        itemUiState.add(ItemDetails(index = itemUiState[itemUiState.size - 1].id + 1, group = groupName, name = ""))
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

data class GroupDetailsUiState(val itemList: List<Item> = listOf())
