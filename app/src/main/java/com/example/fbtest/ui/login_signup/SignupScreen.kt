package com.example.fbtest.ui.login_signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fbtest.R
import com.example.fbtest.ui.destinations.ChatScreenDestination
import com.example.fbtest.ui.destinations.ChatsListDestination
import com.example.fbtest.ui.destinations.LoginScreenDestination
import com.example.fbtest.ui.destinations.SignupScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

/**
 * Created by Taha Ben Ashur (https://github.com/tahaak67) on 20,Feb,2023
 */

@Destination
@Composable
fun SignupScreen(
    navigator: DestinationsNavigator
) {
    val viewModel: AuthenticationViewModel = viewModel()
    if (viewModel.isLoggedIn){
        navigator.navigate(
            ChatScreenDestination
        ){
            popUpTo(SignupScreenDestination.route){
                inclusive = true
            }
        }
    }
    var emailValue by remember {
        mutableStateOf("")
    }
    var passwordValue by remember {
        mutableStateOf("")
    }
    var passVisible by remember {
        mutableStateOf(false)
    }

    Image(
        painter = painterResource(id = R.drawable.background_login_signup),
        contentDescription = stringResource(R.string.background)
    )
    Box(
        modifier = Modifier
            .padding(top = 180.dp)
            .fillMaxSize()
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(25, 0, 0, 0)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(55.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.sign_up),
                    style = MaterialTheme.typography.body2
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = emailValue,
                        onValueChange = { newValue -> emailValue = newValue },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email"
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        label = { Text(text = stringResource(R.string.email)) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = passwordValue,
                        onValueChange = { newValue -> passwordValue = newValue },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Password,
                                contentDescription = "Password"
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { passVisible = !passVisible }) {
                                if (passVisible) {
                                    Icon(
                                        imageVector = Icons.Default.VisibilityOff,
                                        contentDescription = "Visibility off"
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Visibility,
                                        contentDescription = "Visibility"
                                    )
                                }
                            }
                        },
                        visualTransformation = if (passVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        label = { Text(text = stringResource(id = R.string.password)) }
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.forgot_password),
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.h3
                    )
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(onClick = {
                        viewModel.register(emailValue,passwordValue)
                    }) {
                        if (viewModel.isLoading){
                            CircularProgressIndicator()
                        }else{
                            Text(text = stringResource(id = R.string.sign_up))
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        modifier = Modifier
                            .clickable {
                                navigator.navigate(LoginScreenDestination)
                            },
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                )
                            ) {
                                append(stringResource(R.string.already_have_account))
                            }
                            append(" ")
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colors.primaryVariant
                                )
                            ) {
                                append(stringResource(R.string.login_here))
                            }
                        })
                }
            }
        }
    }
}