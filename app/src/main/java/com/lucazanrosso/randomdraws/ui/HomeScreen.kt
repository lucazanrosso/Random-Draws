package com.lucazanrosso.randomdraws.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucazanrosso.randomdraws.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lucazanrosso.randomdraws.NavigationDestination

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToNewGroup: () -> Unit,
    navigateToGroupDetails: (String) -> Unit,
    viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {Text(text = stringResource(HomeDestination.titleRes) )},
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToNewGroup
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(R.string.new_group)
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier
            .padding(innerPadding)
            .padding(start = 8.dp, end = 8.dp)) {

            itemsIndexed(items = homeUiState.group, key = {_, listItem ->
                listItem.hashCode()
            }) { _, item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        var displayMenu by remember { mutableStateOf(false) }

                        Box(modifier = Modifier.padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            val color = MaterialTheme.colorScheme.tertiaryContainer
                            Canvas(modifier = Modifier.size(48.dp), onDraw = {
                                drawCircle(color = color)

                            })
                            Text(text = item.groupName.take(2),
                                fontSize = 24.sp)
                        }
                        Column {
                            Text(text = item.groupName, fontWeight = FontWeight.Bold)
                            Text(text = "Members: " + item.groupCount)
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Box {
                            IconButton(
                                onClick = { displayMenu = true }
                            ) {
                                Icon (
                                    imageVector = Icons.Filled.MoreVert,
                                    contentDescription = stringResource(R.string.delete_group)
                                )
                            }

                            DropdownMenu(
                                expanded = displayMenu,
                                onDismissRequest = { displayMenu = false }
                            ) {
                                DropdownMenuItem(text = { Text(text = "Duplicate") }, onClick = { viewModel.duplicateGroup(item.groupName) })
                                DropdownMenuItem(text = { Text(text = "Edit") }, onClick = { navigateToGroupDetails(item.groupName) })
                                DropdownMenuItem(text = { Text(text = "Delete") }, onClick = { viewModel.deleteGroup(item.groupName) })
                            }
                        }

                    }
                }

            }
        }
    }
}