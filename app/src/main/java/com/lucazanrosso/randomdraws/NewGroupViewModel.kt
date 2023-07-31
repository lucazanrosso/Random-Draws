package com.lucazanrosso.randomdraws

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.ViewModel

class NewGroupViewModel : ViewModel() {

    private var progressiveIdForKeys = mutableStateOf(3)
    var list = mutableStateListOf<ItemDetails>(
        ItemDetails(0, ""),
        ItemDetails(1, ""),
        ItemDetails(2, "")
    )

    fun updateItem(index: Int, name: String) {
        list[index].name = name
    }

    fun addItemToList() {
        list.add(ItemDetails(progressiveIdForKeys.value, ""))
        progressiveIdForKeys.value++
    }

    fun removeItem(index : Int) {
        list.removeAt(index)
    }

//    fun saveListToDb(group: String, list: List<String>) {
//
//    }
}

data class ItemDetails(
    val id: Int = 0,
//    val group: String = "",
    var name: String = "",
)