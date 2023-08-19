package com.lucazanrosso.randomdraws.ui

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

class DrawViewModel (
    savedStateHandle: SavedStateHandle,
    private val dao: ItemDao,
) : ViewModel() {

    var groupName: String = checkNotNull(savedStateHandle[EditGroupDestination.itemIdArg])
    private var extractedItem = mutableStateOf(0)
    var extractedName = mutableStateOf("")

    val notExtractedUiState: StateFlow<DrawUiState> =
        dao.getNotExtractedMembers(groupName)
            .filterNotNull()
            .map{ DrawUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = DrawUiState()
            )

    val extractedUiState: StateFlow<DrawUiState> =
        dao.getExtractedMembers(groupName)
            .filterNotNull()
            .map{ DrawUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = DrawUiState()
            )

    fun randomDraw() {
        extractedItem.value = (0 until notExtractedUiState.value.items.size).random()
        extractedName.value = notExtractedUiState.value.items[extractedItem.value].name
    }

    fun moveToExtracted(index: Int = extractedItem.value) {
        viewModelScope.launch {
            val selectedItem = notExtractedUiState.value.items[index]
            dao.update(selectedItem.copy(extracted = true))
        }
    }

    fun resetDraw() {
        viewModelScope.launch {
            extractedUiState.value.items.forEach{
                dao.update(it.copy(extracted = false))
            }
        }
    }

    fun duplicateGroup(groupName: String, newGroupNameToDuplicate: String) {
        viewModelScope.launch {
            dao.duplicateGroup(groupName, newGroupNameToDuplicate )
        }
    }

    fun deleteGroup (groupName: String) {
        viewModelScope.launch {
            dao.deleteGroup(groupName)
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                DrawViewModel(
                    createSavedStateHandle(),
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RandomDrawsApplication).dao
                )
            }
        }
    }
}
data class DrawUiState(val items: List<Item> = listOf())