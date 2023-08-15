package com.lucazanrosso.randomdraws.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.lucazanrosso.randomdraws.RandomDrawsApplication
import com.lucazanrosso.randomdraws.data.Item
import com.lucazanrosso.randomdraws.data.ItemDao
import kotlinx.coroutines.launch

class NewGroupViewModel(
    private val dao: ItemDao,
) : ViewModel() {

    private var progressiveIdForKeys = mutableStateOf(3)
    var group = mutableStateOf("")
    var list = mutableStateListOf(
        ItemDetails(0, 0, "", ""),
        ItemDetails(1, 1, "", ""),
        ItemDetails(2, 2, "", "")
    )

    fun updateItem(index: Int, name: String) {
        list[index].name = name
    }

    fun addItemToList(index: Int) {
        list.add(index, ItemDetails(progressiveIdForKeys.value, name = ""))
        progressiveIdForKeys.value++
    }

    fun removeItem(index : Int) {
        list.removeAt(index)
    }

    fun saveListToDb() {
        viewModelScope.launch {
            list.forEach {
                if (it.name.isNotEmpty())
                    dao.insert(Item(group = group.value, name = it.name))
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                NewGroupViewModel(
                    (this[APPLICATION_KEY] as RandomDrawsApplication).dao
                )
            }
        }
    }

}

/**
 * Represents Ui State for an Item.
 */
data class ItemUiState(
    val itemDetails: ItemDetails = ItemDetails(),
    val isEntryValid: Boolean = false
)

data class ItemDetails(
    val id: Int = 0,
    val index: Int = 0,
    val group: String = "",
    var name: String = "",
)

fun ItemDetails.toItem(): Item = Item(
    id = id,
    name = name,
    group = group
)