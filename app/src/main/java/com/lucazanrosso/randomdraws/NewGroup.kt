package com.lucazanrosso.randomdraws

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.lucazanrosso.randomdraws.data.AppDatabase
import com.lucazanrosso.randomdraws.data.Item
import com.lucazanrosso.randomdraws.data.ItemDao
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewGroupScreen(
    navController: NavController,
    db : AppDatabase
) {
    val dao = db.itemDao()
    var groupName by rememberSaveable { mutableStateOf("") }
    var list = remember { mutableStateListOf<String>("","","") }
    var onClickSave by remember { mutableStateOf(false) }

    LazyColumn() {
        item {
            OutlinedTextField(
            value = groupName,

            label = { Text(text = "Group name") },
            onValueChange = {
                groupName = it
            }
        ) }

        itemsIndexed(items = list) { index, item ->
            NewItem(index, list, onClickSave, dao, groupName)
        }

        item {
            IconButton(onClick = { list.add("") }) {
                Icon(Icons.Rounded.Add, contentDescription = stringResource(R.string.drag_and_drop))
            }
        }
        item {
            Button(onClick = {
                onClickSave = true
//                list.forEach {
//                    dao.insert(Item(group = groupName, name = it))
//                }
                navController.popBackStack()
            }) {
                Text(text = "Save")
            }
        }

        item {
            Button(onClick = {
                println(groupName)
                println(list.size)
                list.forEach {
                    println(it)
                }
            }) {
                Text(text = "Print")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewItem(
    index: Int,
    list: SnapshotStateList<String>,
    onSaveClick: Boolean,
    dao : ItemDao,
    groupName: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ){
        var text by rememberSaveable { mutableStateOf(list[index]) }
        val label = index + 1

//        IconButton(onClick = { /* doSomething() */ }) {
//            Icon(Icons.Rounded.Menu, contentDescription = stringResource(R.string.drag_and_drop))
//        }

        OutlinedTextField(
            value = text,

            label = { Text(text = "$label") },
            onValueChange = {
                text = it
                list[index] = text
            }
        )

        IconButton(onClick = { list.removeAt(index) }) {
            Icon(Icons.Rounded.Clear, contentDescription = stringResource(R.string.drag_and_drop))
        }

        if (onSaveClick && text.isNotEmpty()) dao.insert(Item(group = groupName, name = text))

    }
}