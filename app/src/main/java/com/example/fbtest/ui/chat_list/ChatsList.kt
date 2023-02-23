package com.example.fbtest.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fbtest.model.Conversation
import com.example.fbtest.R
import com.example.fbtest.ui.chat_list.ChatListViewModel
import com.example.fbtest.ui.destinations.ChatScreenDestination
import com.example.fbtest.ui.destinations.NewConversationScreenDestination
import com.example.fbtest.ui.theme.Gray200
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

/**
 * Created by Taha Ben Ashur (https://github.com/tahaak67) on 20,Feb,2023
 */

@Destination
@Composable
fun ChatsList(
    navigator: DestinationsNavigator
) {
    val viewModel: ChatListViewModel = viewModel()
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp),
        title = { Text(text = "Home", textAlign = TextAlign.Center) },
        navigationIcon = {
            IconButton(onClick = { // todo:
            }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "search")
            }
        },
        backgroundColor = Color.White
    )
    val conversations = listOf(
        Conversation("where is you", listOf("Mohammed")),
        Conversation("Do you like peely?", listOf("Its show time!")),
        Conversation("Salam!", listOf("Khaled")),
    )
    Box(
        modifier = Modifier
            .padding(top = 58.dp)
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(viewModel.conversations.value) { conversation ->
                ConversationItem(conversation)
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .padding(50.dp)
                .align(Alignment.BottomEnd),
            onClick = {
                navigator.navigate(NewConversationScreenDestination)
            }) {
            Icon(imageVector = Icons.Default.Message, contentDescription = "new message")
        }
    }
}

@Composable
private fun ConversationItem(conversation: Conversation) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 15.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Image(
                modifier = Modifier
                    .clip(CircleShape),
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "user picture",
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = conversation.contact.first(), style = MaterialTheme.typography.h1)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = conversation.lastMessage, style = MaterialTheme.typography.h3)
            }
        }
        Column {
            Text(text = "2 min", style = MaterialTheme.typography.h3, color = Gray200)
        }
    }
}