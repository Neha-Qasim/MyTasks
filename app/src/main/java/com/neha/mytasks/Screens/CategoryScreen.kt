package com.neha.mytasks.Screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.neha.mytasks.auth.AuthViewModel
import com.neha.mytasks.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CategoryScreen(
    navController: NavController,
    viewModel: TaskViewModel,
    authViewModel: AuthViewModel
) {
    val categories by viewModel.categories.collectAsState()
    val tasks by viewModel.tasks.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var showAddDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("") }
    var newCategory by remember { mutableStateOf("") }

    //  Force fresh data fetch when this screen loads
    LaunchedEffect(Unit) {
        viewModel.refreshData()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Categories") },
                actions = {
                    IconButton(onClick = {
                        authViewModel.signOut()
                        navController.navigate("sign_in") {
                            popUpTo("categories") { inclusive = true }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Logout",
                            tint = Color.Black
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Category")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(categories) { category ->
                val taskCount = tasks.count { it.category == category }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .combinedClickable(
                            onClick = {
                                navController.navigate("tasks/$category")
                            },
                            onLongClick = {
                                selectedCategory = category
                                showDeleteDialog = true
                            }
                        ),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = category.replaceFirstChar { it.uppercase() },
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$taskCount ${if (taskCount == 1) "task" else "tasks"}",
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }

        // ➕ Add Category Dialog
        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = {
                    showAddDialog = false
                    newCategory = ""
                },
                title = { Text("Add Category") },
                text = {
                    OutlinedTextField(
                        value = newCategory,
                        onValueChange = { newCategory = it },
                        label = { Text("Category Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        val trimmed = newCategory.trim()
                        if (trimmed.isNotBlank()) {
                            val alreadyExists = categories.any { it.equals(trimmed, ignoreCase = true) }
                            if (alreadyExists) {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Category \"$trimmed\" already exists.")
                                }
                            } else {
                                viewModel.addCategory(trimmed)
                                scope.launch {
                                    snackbarHostState.showSnackbar("Added \"$trimmed\".")
                                }
                            }
                            newCategory = ""
                            showAddDialog = false
                        }
                    }) {
                        Text("Add")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showAddDialog = false
                        newCategory = ""
                    }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // 🗑 Delete Category Dialog
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Category?") },
                text = {
                    Text("Are you sure you want to delete \"$selectedCategory\" and all its tasks?")
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.deleteCategory(selectedCategory)
                        scope.launch {
                            snackbarHostState.showSnackbar("Deleted \"$selectedCategory\".")
                        }
                        showDeleteDialog = false
                    }) {
                        Text("Delete", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
