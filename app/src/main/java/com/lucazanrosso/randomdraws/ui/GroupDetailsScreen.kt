package com.lucazanrosso.randomdraws.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lucazanrosso.randomdraws.R

object GroupDetailsDestination : NavigationDestination {
    override val route = "group_details"
    override val titleRes = R.string.group_details
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDetailsScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GroupDetailsViewModel = viewModel(factory = GroupDetailsViewModel.Factory)
) {
    val list = viewModel.itemUiState

    list.forEach{
        println("${it.index} ${it.name}")
    }
    println("${viewModel.groupName} ${viewModel.newGroupName}")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(GroupDetailsDestination.titleRes) ) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.upsertItems()
                        viewModel.deleteItemsFromDb()
                        navigateBack()
                    }){
                        Icon(
                            imageVector = Icons.Rounded.Done,
                            contentDescription = "Localized description"
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(start = 16.dp, end = 16.dp)
        ) {
            item {
                var text by rememberSaveable { mutableStateOf(viewModel.newGroupName) }
                OutlinedTextField(
                    value = text,
                    label = { Text(text = "Group name") },
                    onValueChange = {
                        text = it
                        viewModel.newGroupName = text},
                    modifier = Modifier.fillParentMaxWidth()
                )
            }

            itemsIndexed(items = list, key = { _, listItem ->
                listItem.index }) { index, item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier.fillMaxWidth()
                ){
                    var text by rememberSaveable { mutableStateOf(item.name) }
                    val label = index + 1

                    OutlinedTextField(
                        value = text,
                        label = { Text(text = "$label") },
                        onValueChange = {
                            text = it
                            viewModel.updateItem(index, text)
                        },
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = { viewModel.deleteItem(item) },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Icon(Icons.Rounded.Clear, contentDescription = stringResource(R.string.drag_and_drop))
                    }
                }
            }

            item {
                IconButton(
                    onClick = { viewModel.addItemToList(viewModel.itemUiState.size) },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Icon(Icons.Rounded.Add, contentDescription = stringResource(R.string.drag_and_drop))
                }
            }
        }
    }
}
