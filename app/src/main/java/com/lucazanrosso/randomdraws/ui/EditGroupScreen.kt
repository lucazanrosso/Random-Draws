package com.lucazanrosso.randomdraws.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lucazanrosso.randomdraws.NavigationDestination
import com.lucazanrosso.randomdraws.R

object EditGroupDestination : NavigationDestination {
    override val route = "group_details"
    override val titleRes = R.string.edit_group
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGroupScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditGroupViewModel = viewModel(factory = EditGroupViewModel.Factory)
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(EditGroupDestination.titleRes) ) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.upsertItems()
                            viewModel.deleteItemsFromDb()
                            navigateBack()
                        },
                        enabled = viewModel.isValid
                    ){
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
                .padding(start = 12.dp, end = 12.dp)
        ) {
            item {
                OutlinedTextField(
                    value = viewModel.newGroupName,
                    label = { Text(text = "Group name") },
                    onValueChange = { viewModel.updateGroupName(it) },
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                    modifier = Modifier.fillParentMaxWidth().padding(4.dp)
                )
            }

            itemsIndexed(
                items = viewModel.list,
                key = { _, listItem -> listItem.index })
            { index, item ->
                Box(
                    modifier = modifier.fillMaxWidth().padding(4.dp)
                ) {

                    var text by rememberSaveable { mutableStateOf(item.name) }
                    val label = index + 1
                    var showDelete by rememberSaveable { mutableStateOf(false) }

                    OutlinedTextField(
                        value = text,
                        label = { Text(text = "$label") },
                        onValueChange = {
                            text = it
                            viewModel.updateItem(index, text)
                        },
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                        modifier = Modifier
                            .onFocusChanged { showDelete = it.isFocused }
                            .fillMaxWidth()
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier.fillMaxWidth()
                    ) {
                        Spacer(modifier = Modifier.weight(1f))

                        if (showDelete) {
                            IconButton(
                                onClick = { viewModel.deleteItem(item) },
                                modifier = Modifier.padding(top = 11.dp)
                            ) {
                                Icon(
                                    Icons.Rounded.Clear,
                                    contentDescription = stringResource(R.string.delete_item)
                                )
                            }
                        }
                    }
                }
            }

            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = { viewModel.addItemToList(viewModel.list.size) },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Icon(
                            Icons.Rounded.Add, contentDescription = stringResource(R.string.add_item)
                        )
                    }
                }
            }
        }
    }
}
