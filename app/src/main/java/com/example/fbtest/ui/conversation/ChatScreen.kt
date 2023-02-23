package com.example.fbtest.ui.conversation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.example.fbtest.R
import com.example.fbtest.model.Message
import com.example.fbtest.ui.destinations.ChatScreenDestination
import com.example.fbtest.ui.destinations.LoginScreenDestination
import com.example.fbtest.ui.login_signup.AuthenticationViewModel
import com.example.fbtest.ui.theme.Gray200
import com.example.fbtest.ui.theme.Orange500
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import ly.com.tahaben.showcase_layout_compose.model.Arrow
import ly.com.tahaben.showcase_layout_compose.model.ShowcaseMsg
import ly.com.tahaben.showcase_layout_compose.model.Side
import ly.com.tahaben.showcase_layout_compose.ui.ShowcaseLayout

/**
 * Created by Taha Ben Ashur (https://github.com/tahaak67) on 20,Feb,2023
 */

@RootNavGraph(start = true)
@Destination
@Composable
fun ChatScreen(
    navigator: DestinationsNavigator,
) {
    var imageUri by rememberSaveable {
        mutableStateOf<Uri?>(null)
    }
    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> imageUri = uri }
    )
    val viewModel: ChatViewModel = viewModel()
    val authViewModel: AuthenticationViewModel = viewModel()
    val state = viewModel.state
    val context = LocalContext.current
    if (!authViewModel.isLoggedIn) {
        navigator.navigate(
            LoginScreenDestination
        ) {
            popUpTo(ChatScreenDestination.route) {
                inclusive = true
            }
        }
    }
    var isShowcasing by remember {
        mutableStateOf(true)
    }
    imageUri?.let {

        val inputStream = context.contentResolver.openInputStream(it)
        val bytes = inputStream?.readBytes()
        inputStream?.close()

        if (bytes != null) {
            viewModel.uploadImage(it, bytes)
        }
    }

    ShowcaseLayout(
        isShowcasing = isShowcasing,
        onFinish = { isShowcasing = false },
        greeting = ShowcaseMsg(
            "Welcome to MyChat, tap anywhere to continue!",
            msgBackground = Color.White
        )
    ) {
        TopAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            title = { /*Text(text = messageTo, textAlign = TextAlign.Center)*/ },
            navigationIcon = {
                /*IconButton(onClick = {
                    navigator.navigateUp()
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
                }*/
            },
            actions = {
                Image(
                    modifier = Modifier
                        .clip(CircleShape),
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "user picture",
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = "Group", style = MaterialTheme.typography.h1)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Online now", style = MaterialTheme.typography.h3)
                }
            },
            backgroundColor = Color.White
        )
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(top = 58.dp)
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(viewModel.messages) { message ->
                    MessageItem(message, state)
                }
            }
            var msgValue by remember {
                mutableStateOf("")
            }
            Showcase(
                k = 1, message = ShowcaseMsg(
                    "Write your message here",
                    arrow = Arrow(
                        hasHead = false,
                        targetFrom = Side.Top
                    ),
                    msgBackground = Color.White
                )
            ) {
                TextField(
                    modifier = Modifier
                        .height(80.dp)
                        .fillMaxWidth(),
                    value = msgValue,
                    onValueChange = { msgValue = it },
                    trailingIcon = {
                        Showcase(
                            k = 2, message = ShowcaseMsg(
                                "Press this button to send your message!",
                                msgBackground = Color.White
                            )
                        ) {
                            IconButton(onClick = {
                                viewModel.sendMessage("", msgValue)
                                msgValue = ""
                            }) {
                                Image(
                                    painter = painterResource(id = R.drawable.send_btn),
                                    contentDescription = "send"
                                )
                            }
                        }
                    },
                    label = { Text(text = "Type your message...") },
                    leadingIcon = {
                        Showcase(
                            k = 3, message = ShowcaseMsg(
                                "Add an image from here",
                                msgBackground = Color.White,
                            )
                        ) {
                            IconButton(onClick = {
                                imageLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "attach media"
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun MessageItem(
    message: Message,
    state: ChatScreenState
) {
    Column {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Card(
                modifier = Modifier
                    .padding(top = 10.dp, start = 18.dp, end = 18.dp)
                    .width(maxWidth / 2)
                    .align(
                        if (message.from == state.myEmail) {
                            Alignment.CenterEnd
                        } else {
                            Alignment.CenterStart
                        }
                    ),
                shape = if (message.from == state.myEmail) {
                    RoundedCornerShape(20, 20, 0, 20)
                } else {
                    RoundedCornerShape(20, 20, 20, 0)

                }
            ) {
                Column(
                    modifier = Modifier
                        .background(
                            if (message.from == state.myEmail) {
                                Gray200
                            } else {
                                Orange500
                            }
                        )
                        .padding(12.dp),
                ) {
                    Text(
                        text = message.content,
                        style = MaterialTheme.typography.h2,
                        color = if (message.from == state.myEmail) {
                            Color.Black
                        } else {
                            Color.White
                        }
                    )
                    if (message.imageUrl != null) {
                        SubcomposeAsyncImage(
                            modifier = Modifier
                                .padding(24.dp),
                            model = message.imageUrl,
                            contentDescription = message.content
                        ) {
                            val painterState = painter.state
                            if (painterState is AsyncImagePainter.State.Loading || painterState is AsyncImagePainter.State.Error) {
                                CircularProgressIndicator()
                            } else {
                                SubcomposeAsyncImageContent()
                            }
                        }
                    }
                }
            }
        }

        if (message.from != state.myEmail) {
            //Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier.padding(horizontal = 18.dp),
                text = message.from,
                style = MaterialTheme.typography.h3
            )
        }
    }
}