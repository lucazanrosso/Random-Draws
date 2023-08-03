package com.lucazanrosso.randomdraws.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.lucazanrosso.randomdraws.RandomDrawsApplication
import com.lucazanrosso.randomdraws.data.ItemDao

class GroupsViewModel (
    private val dao: ItemDao,
) : ViewModel() {

    var groupsAndCount = dao.getGroups()
    var groups = groupsAndCount.keys.toList().toMutableStateList()
    var count = groupsAndCount.values.toList().toMutableStateList()

    fun deleteGroup (group: String, index: Int) {
        groups.removeAt(index)
        count.removeAt(index)
        dao.deleteGroup(group)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                GroupsViewModel(
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RandomDrawsApplication).dao
                )
            }
        }
    }

}


data class GroupCount(
    val id: Int = 0,
    val group: String = "",
    var count: Int = 0,
)