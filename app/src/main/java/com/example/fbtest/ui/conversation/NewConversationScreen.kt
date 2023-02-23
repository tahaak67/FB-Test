package com.example.fbtest.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fbtest.ui.conversation.ConversationsViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

/**
 * Created by Taha Ben Ashur (https://github.com/tahaak67) on 20,Feb,2023
 */
@Destination
@Composable
fun NewConversationScreen(
    navigator: DestinationsNavigator
) {
    val viewModel: ConversationsViewModel = viewModel()
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp),
        title = { Text(text = "Select a contact") },
        navigationIcon = {
            IconButton(onClick = {
                navigator.navigateUp()
            }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
            }
        },
        backgroundColor = Color.White
    )
    LazyColumn(
        modifier = Modifier
            .padding(top = 58.dp)
            .fillMaxSize()
    ) {
        items(viewModel.contacts){contact ->
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = contact.email)
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}