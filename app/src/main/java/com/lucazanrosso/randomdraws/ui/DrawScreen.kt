package com.lucazanrosso.randomdraws.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
    modifier: Modifier = Modifier,
    viewModel: DrawViewModel = viewModel(factory = DrawViewModel.Factory)
) {
    val notExtractedUiState by viewModel.notExtractedUiState.collectAsState()
    val extractedUiState by viewModel.extractedUiState.collectAsState()
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var showEditDraw by rememberSaveable { mutableStateOf(false) }
    var showResetDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = viewModel.groupName ) },
                navigationIcon = {
                    if (!showEditDraw) {
                        IconButton(onClick = navigateBack) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowBack,
                                contentDescription = stringResource(R.string.back_button)
                            )
                        }
                    } else {
                        IconButton(onClick = { showEditDraw = false }) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowBack,
                                contentDescription = stringResource(R.string.back_button)
                            )
                        }
                    }
                },
                actions = {
                    if (!showEditDraw) {
                        IconButton(onClick = {
                            showEditDraw = true
                        }) {
                            Icon(
                                imageVector = Icons.Rounded.Edit,
                                contentDescription = stringResource(R.string.edit_draw)
                            )
                        }
                    }
                    else {
                        IconButton(onClick = {
                            showResetDialog = true
                        }) {
                            Icon(
                                imageVector = Icons.Rounded.Refresh,
                                contentDescription = stringResource(R.string.reset_draw)
                            )
                        }
                    }
                }

            )
        },
        floatingActionButton = {
            if (!showEditDraw and notExtractedUiState.items.isNotEmpty()) {
                FloatingActionButton(
                    onClick = {
                        viewModel.randomDraw()
                        showDialog = true
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
                }) { index, item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier.fillMaxWidth()
                    ) {

                        Text(
                            text = item.name,
                            modifier = modifier.padding(16.dp)
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        if (showEditDraw) {
                            IconButton(
                                onClick = { viewModel.moveToExtracted(index) },
                            ) {
                                Icon(
                                    Icons.Rounded.KeyboardArrowDown,
                                    contentDescription = stringResource(R.string.drag_and_drop)
                                )
                            }
                        }
                    }
                }
            }

            itemsIndexed(
                items = extractedUiState.items,
                key = {_, listItem -> listItem.hashCode()
            }) { index, item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier.fillMaxWidth()
                    ) {

                        Text(
                            text = item.name,
                            fontSize = 16.sp,
                            style = TextStyle(textDecoration = TextDecoration.LineThrough),
                            modifier = modifier.padding(16.dp)

                        )

                        Spacer(modifier = Modifier.weight(1f))

                        if (showEditDraw) {
                            IconButton(
                                onClick = { viewModel.moveToNotExtracted(index) },
                            ) {
                                Icon(
                                    Icons.Rounded.KeyboardArrowUp,
                                    contentDescription = stringResource(R.string.drag_and_drop)
                                )
                            }
                        }
                    }
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(viewModel.extractedName.value) },
                text = { Text(text = stringResource(R.string.was_extracted)) },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.moveToExtracted()
                        showDialog = false
                    }) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
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
    }
}
