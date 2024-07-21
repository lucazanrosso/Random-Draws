package com.lucazanrosso.randomdraws.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lucazanrosso.randomdraws.NavigationDestination
import com.lucazanrosso.randomdraws.R

object EditDrawDestination : NavigationDestination {
    override val route = "edit_draw"
    override val titleRes = R.string.edit_draw
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDrawScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditDrawViewModel = viewModel(factory = EditDrawViewModel.Factory)
) {
    val notExtractedUiState by viewModel.notExtractedUiState.collectAsState()
    val extractedUiState by viewModel.extractedUiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(R.string.edit_draw) ) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                }
            )
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
                }) { index, item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    )
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
}