package com.lucazanrosso.randomdraws.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lucazanrosso.randomdraws.NavigationDestination
import com.lucazanrosso.randomdraws.R

object DrawDestination : NavigationDestination {
    override val route = "draw"
    override val titleRes = R.string.draw
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawScreen(
    navigateBack: () -> Unit,
    navigateToEditDraw: (String) -> Unit,
    navigateToEditGroup: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DrawViewModel = viewModel(factory = DrawViewModel.Factory)
) {
    val notExtractedUiState by viewModel.notExtractedUiState.collectAsState()
    val extractedUiState by viewModel.extractedUiState.collectAsState()
    var showExtractedDialog by rememberSaveable { mutableStateOf(false) }
    var showResetDialog by rememberSaveable { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var showDuplicateDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var newGroupNameToDuplicate by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = viewModel.groupName ) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        showMenu = true
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.MoreVert,
                            contentDescription = stringResource(R.string.edit_draw)
                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.edit_draw)) },
                            onClick = { navigateToEditDraw(viewModel.groupName) })
                        DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.reset_draw)) },
                            onClick = {
                                showResetDialog = true
                                showMenu = false
                            })
                        DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.duplicate)) },
                            onClick = {
                                showDuplicateDialog = true
                                showMenu = false
                            })
                        DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.edit_group)) },
                            onClick = { navigateToEditGroup(viewModel.groupName) })
                        DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.delete_group)) },
                            onClick = {
                                showDeleteDialog = true
                                showMenu = false
                            })
                    }
                }
            )
        },
        floatingActionButton = {
            if (notExtractedUiState.items.isNotEmpty()) {
                FloatingActionButton(
                    onClick = {
                        viewModel.randomDraw()
                        showExtractedDialog = true
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_casino_24),
                        contentDescription = stringResource(R.string.new_group)
                    )
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(start = 8.dp, end = 8.dp)
        ) {

            itemsIndexed(
                items = notExtractedUiState.items,
                key = {_, listItem -> listItem.hashCode()
                }) { _, item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    )
                ) {
                    Text(
                        text = item.name,
                        modifier = modifier.padding(16.dp)
                    )
                }
            }

            if((extractedUiState.items.isNotEmpty() and notExtractedUiState.items.isNotEmpty())) {
                item {
                    Divider(
                        thickness = 1.dp,
                        modifier = modifier.padding(
                            top = 16.dp,
                            bottom = 16.dp,
                            start = 4.dp,
                            end = 4.dp
                        )
                    )
                }
            }

            itemsIndexed(
                items = extractedUiState.items,
                key = {_, listItem -> listItem.hashCode()
            }) { _, item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ) {
                    Text(
                        text = item.name,
                        fontSize = 16.sp,
                        style = TextStyle(textDecoration = TextDecoration.LineThrough),
                        modifier = modifier.padding(16.dp)

                    )
                }
            }
        }

        if (showExtractedDialog) {
            AlertDialog(
                onDismissRequest = { showExtractedDialog = false },
                title = { Text(viewModel.extractedName.value) },
                text = { Text(text = stringResource(R.string.was_extracted)) },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.moveToExtracted()
                        showExtractedDialog = false
                    }) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showExtractedDialog = false }) {
                        Text("Cancel")
                    }
                },

            )
        }

        if (showResetDialog) {
            AlertDialog(
                onDismissRequest = { showResetDialog = false },
                title = { Text(stringResource(R.string.reset_draw)) },
                text = { Text(text = stringResource(R.string.reset_draws_dialog_text)) },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.resetDraw()
                        showResetDialog = false
                    }) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showResetDialog = false }) {
                        Text("Cancel")
                    }
                },

            )
        }

        if (showDuplicateDialog) {
            AlertDialog(
                onDismissRequest = { showDuplicateDialog = false },
                title = { Text(text = stringResource(R.string.new_group_name)) },
                text = {
                    Column {
                        Text(text = stringResource(R.string.new_group_name_dialog_text))
                        OutlinedTextField(
                            value = newGroupNameToDuplicate,
                            onValueChange = { newGroupNameToDuplicate = it },
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.duplicateGroup(viewModel.groupName, newGroupNameToDuplicate)
                            showDuplicateDialog = false
                            navigateBack()
                        },
                        enabled = newGroupNameToDuplicate.isNotEmpty()
                    ) {
                        Text(stringResource(R.string.confirm))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDuplicateDialog = false
                        }) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            )
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text(text = stringResource(R.string.delete_group)) },
                text = { Text(text = stringResource(R.string.delete_group_dialog_text)) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteGroup(viewModel.groupName)
                            showDeleteDialog = false
                            navigateBack()
                        },
                    ) {
                        Text(stringResource(R.string.confirm))
                    }
                },
                dismissButton = {
                    TextButton( onClick = { showDeleteDialog = false })
                    {
                        Text(stringResource(R.string.cancel))
                    }
                }
            )
        }
    }
}
