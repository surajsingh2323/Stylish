package com.example.stylish.presentation.Auth

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.stylish.Navigation.Routes
import com.example.stylish.domain.util.Result

@Composable
fun ForgetPasswordScreen(NavHostController: NavHostController){
    // --- State ---
    val viewModel: AuthViewModel = hiltViewModel()
    val authState by viewModel.authState.collectAsState()
    var userForgotEmail by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }

    // --- Side Effect: Navigate on Success ---
    LaunchedEffect(authState) {
        if (authState is Result.Success) {
            NavHostController.navigate(Routes.LoginScreen)
            viewModel.resetAuthState()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
            .imePadding(),                        // ← keyboard handling,
        // imePadding() fixes this — IME = Input Method Editor = keyboard.
        // It automatically adds bottom padding equal to the keyboard height when keyboard opens.
        verticalArrangement = Arrangement.spacedBy(25.dp)
    ){
        Column {
            Spacer(modifier = Modifier.height(80.dp))
            Text(
                "Forgot\n\npassword?",
                fontSize = 42.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(start = 15.dp),
                letterSpacing = 0.5.sp
            )
        }
        // Middle Section - Input Fields and Login Button
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Username/Email Input Field
            Box(
                modifier = Modifier.clip(shape = RoundedCornerShape(10.dp))
                    .border(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFB2B2B2),
                        width = 2.dp
                    )
                    .height(70.dp)
                    .fillMaxWidth()
                    .background(color = Color(0xFFF3F3F3))
            ){
                Icon(
                    Icons.Default.Email,
                    contentDescription = "Email Icon",
                    modifier = Modifier.padding(start = 15.dp)
                        .align(Alignment.CenterStart)
                        .size(25.dp)
                )
                TextField(
                    value = userForgotEmail,
                    onValueChange = {userForgotEmail = it
                        emailError = ""           // clear error as user types
                        },
                    modifier = Modifier.padding(start = 45.dp, end = 45.dp)
                        .fillMaxSize(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    singleLine = true,
                    placeholder = {
                        Text(
                            text = "Enter your e-mail address",
                            fontSize = 14.sp
                        )
                    },
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        letterSpacing = 0.5.sp,
                        color = Color.Black
                    )
                )
            }

            // Validation error
            if (emailError.isNotEmpty()) {
                Text(emailError, color = Color.Red, fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.Start).padding(start = 8.dp, top = 4.dp))
            }

            // API error
            if (authState is Result.Failure) {
                Text((authState as Result.Failure).message,
                    color = Color.Red, fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp))
            }

            Spacer(modifier= Modifier.height(20.dp))

            Text(
                text = "we will send you a message to set or reset your new password",
                fontSize = 14.sp,
                color = Color.Gray,
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(start = 8.dp,end = 8.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))
            Box(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(5.dp))
                    .height(70.dp)
                    .fillMaxWidth()
                    .background(Color(0xFF8817C6))
                    .clickable {
                        if (userForgotEmail.isEmpty()) {
                            emailError = "Email cannot be empty"
                        } else if (!Patterns.EMAIL_ADDRESS
                                .matcher(userForgotEmail).matches()) {
                            emailError = "Invalid email format"
                        } else {
                            emailError = ""
                            viewModel.sendPasswordReset(userForgotEmail)
                        }
                    }
            ) {
                if (authState is Result.Loading) {
                    CircularProgressIndicator(color = Color.White,
                        modifier = Modifier.align(Alignment.Center).size(28.dp))
                }
                else{
                Text(
                    text = "Submit",
                    fontSize = 24.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.Center),
                    letterSpacing = 0.5.sp
                )
            }
         //   Spacer(modifier = Modifier.height(400.dp)); not needed as using imePadding.

        }
    }
}// Patterns.EMAIL_ADDRESS -> It's a built-in Android regex pattern that checks if a string looks like a valid email.
}