package com.lucazanrosso.randomdraws

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.lucazanrosso.randomdraws.data.AppDatabase
import com.lucazanrosso.randomdraws.data.Item
import kotlinx.coroutines.flow.*
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewGroupScreen(
    navController: NavController,
    db : AppDatabase,
    viewModel: NewGroupViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val dao = db.itemDao()
    var onClickSave by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        item {
            OutlinedTextField(
                value = viewModel.group.value,
                label = { Text(text = "Group name") },
                onValueChange = {viewModel.group.value = it },
                modifier = Modifier.fillParentMaxWidth()
            )
        }

        itemsIndexed(items = viewModel.list, key = {_, listItem ->
            listItem.id
        }) { index, item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.fillMaxWidth()
            ){
                var text by rememberSaveable { mutableStateOf(item.name) }
                val label = index + 1

                IconButton(onClick = {
                    viewModel.addItemToList(index)
                }) {
                    Icon(Icons.Rounded.Add, contentDescription = stringResource(R.string.drag_and_drop))
                }

                OutlinedTextField(
                    value = text,
                    label = { Text(text = "$label") },
                    onValueChange = {
                        text = it
                        viewModel.updateItem(index, text)
                    },
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = { viewModel.removeItem(index) }) {
                    Icon(Icons.Rounded.Clear, contentDescription = stringResource(R.string.drag_and_drop))
                }

                if (onClickSave && text.isNotEmpty()) dao.insert(Item(group = viewModel.group.value, name = text))
            }
        }

        item {
            IconButton(onClick = {
                viewModel.addItemToList(viewModel.list.size)
            }) {
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
    }
}