package com.codewithfk.zegocloudvideocall.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun LoginScreen(navController: NavController) {
    val viewModel: LoginViewModel = hiltViewModel()
    val userName = remember {
        mutableStateOf("")
    }
    val passWord = remember {
        mutableStateOf("")
    }

    val loading = remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(26.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Text(text = "Sign Up")
        OutlinedTextField(
            value = userName.value,
            onValueChange = { userName.value = it },
            label = { Text(text = "Username") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = passWord.value,
            onValueChange = { passWord.value = it },
            label = { Text(text = "Password") },
            modifier = Modifier.fillMaxWidth()
        )

        if (loading.value) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    viewModel.login(userName.value, passWord.value)
                }, modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Login")
            }

            TextButton(onClick = { navController.navigate("signup") }) {
                Text(text = "Don't have an account? Sign up")
            }
        }
    }
    val state = viewModel.loginStateFlow.collectAsState()
    LaunchedEffect(state.value) {
        when (state.value) {
            is LoginState.Normal -> {
                loading.value = false
            }

            is LoginState.Loading -> {
                // show loading
                loading.value = true
            }

            is LoginState.Success -> {
                navController.navigate("home") {
                    popUpTo("login") {
                        inclusive = true
                    }
                }
            }

            is LoginState.Error -> {
                loading.value = false
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(rememberNavController())
}