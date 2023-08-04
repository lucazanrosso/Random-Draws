package com.lucazanrosso.randomdraws.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.lucazanrosso.randomdraws.RandomDrawsApplication
import com.lucazanrosso.randomdraws.data.Item
import com.lucazanrosso.randomdraws.data.ItemDao
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.map
import androidx.lifecycle.viewModelScope
import com.lucazanrosso.randomdraws.data.Group
import kotlinx.coroutines.flow.SharingStarted

class GroupsViewModel (
    private val dao: ItemDao,
) : ViewModel() {

//    var groupsAndCount = dao.getGroups().toMutableMap()
//    var list = mutableStateListOf(
//        GroupCount(0, "4EM", 1),
//        GroupCount(0, "4EM", 1),
//        GroupCount(0, "4EM", 1)
//    )
//    var groups = groupsAndCount.keys.toList().toMutableStateList()
//    var count = groupsAndCount.values.toList().toMutableStateList()
//
//    fun createGroups() {
//        var id = 0
//        groupsAndCount.forEach {
//            list.add(GroupCount(/*id = id++,*/ group = it.key, count = it.value))
//        }
//    }

    fun deleteGroup (index: Int) {
//        dao.deleteGroup(groups[index])
//        groups.removeAt(index)

//        groups.removeAt(index)

    }

//    val homeUiState: StateFlow<HomeUiState> =
//        dao.getGroups().map { it.map { HomeUiState(it.key, it.value) } }
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
//                initialValue = HomeUiState()
//            )
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

data class HomeUiState(val group: List<Group> = listOf()) {

}

//data class GroupCount(
//    val id: Int = 0,
//    val group: String = "",
//    var count: Int = 0,
//)