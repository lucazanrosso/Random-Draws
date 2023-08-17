package com.lucazanrosso.randomdraws.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
    var isValid by mutableStateOf(false)

    private fun validateInput () {
        if (group.value.isEmpty()) isValid = false
        if (list.isEmpty()) isValid = false
        list.forEach {
            if (it.name.isEmpty()) isValid = false
        }
        isValid = true
    }

    fun updateGroupName(groupName: String) {
        group.value = groupName
        validateInput()
    }

    fun updateItem(index: Int, name: String) {
        list[index].name = name
        validateInput()
    }

    fun addItemToList(index: Int) {
        list.add(index, ItemDetails(progressiveIdForKeys.value, name = ""))
        progressiveIdForKeys.value++
        validateInput()
    }

    fun removeItem(item: ItemDetails) {
        list.remove(item)
        validateInput()
    }

    fun saveListToDb() {
        viewModelScope.launch {
            list.forEach {
                if (it.name.isNotEmpty())
                    dao.insert(Item(group = group.value, name = it.name, extracted = false))
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

data class ItemDetails(
    val id: Int = 0,
    val index: Int = 0,
    var group: String = "",
    var name: String = "",
    var extracted: Boolean = false
)

fun ItemDetails.toItem(): Item = Item(
    id = id,
    name = name,
    group = group,
    extracted = extracted
)