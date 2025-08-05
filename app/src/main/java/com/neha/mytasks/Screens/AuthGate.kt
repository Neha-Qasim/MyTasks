package com.neha.mytasks.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.neha.mytasks.auth.AuthViewModel
import com.neha.mytasks.viewmodel.TaskViewModel

@Composable
fun AuthGate(
    navController: NavController,
    authViewModel: AuthViewModel,
    taskViewModel: TaskViewModel
) {
    val currentUser = authViewModel.currentUser

    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            taskViewModel.refreshData() //  Load fresh data for new user
            navController.navigate("categories") {
                popUpTo("auth_gate") { inclusive = true }
            }
        } else {
            navController.navigate("sign_in") {
                popUpTo("auth_gate") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
