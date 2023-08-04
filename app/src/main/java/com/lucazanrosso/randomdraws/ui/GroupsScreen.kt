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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lucazanrosso.randomdraws.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupScreen(
    navigateToNewGroup: () -> Unit,
    navigateBack: () -> Unit,
    viewModel: GroupsViewModel = viewModel(factory = GroupsViewModel.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {Text(text = stringResource(Destination.Groups.title) )},
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = navigateToNewGroup) {
                        Icon(
                            imageVector = Icons.Rounded.AddCircle,
                            contentDescription = "Localized description"
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
//            item {     Button(onClick = {     viewModel.createGroups() }) {
//
//            } }

            itemsIndexed(items = homeUiState.group, key = {_, listItem ->
                listItem.hashCode()
            }) { index, item ->
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),

                    onClick = { /*TODO*/ }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.padding(8.dp),
                            contentAlignment = Alignment.Center
                            ) {
                            Canvas(modifier = Modifier.size(48.dp), onDraw = {
                                drawCircle(color = Color.White)

                            })
                            Text(text = item.groupName.take(2),
                                fontSize = 24.sp)
                        }
                        Column {
                            Text(text = item.groupName, fontWeight = FontWeight.Bold)
                            Text(text = "Members: " + item.groupCount)
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = { viewModel.deleteGroup(index) }) {
                            Icon (
                                imageVector = Icons.Filled.Delete,
                                contentDescription = stringResource(R.string.delete_group)
                            )

                        }
                    }
                }

            }
        }
    }
}
