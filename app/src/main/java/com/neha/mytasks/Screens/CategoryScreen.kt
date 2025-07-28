package com.neha.mytasks.Screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.neha.mytasks.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

@Composable
fun CategoryScreen(navController: NavController, viewModel: TaskViewModel) {
    val categories by viewModel.categories.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("") }
    var newCategory by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Categories") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                showAddDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Category")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(categories) { category ->
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
                    Text(
                        text = category,
                        modifier = Modifier.padding(16.dp),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Dialog to Add Category
        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text("Add Category") },
                text = {
                    OutlinedTextField(
                        value = newCategory,
                        onValueChange = { newCategory = it },
                        label = { Text("Category Name") }
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (newCategory.isNotBlank()) {
                            viewModel.addCategory(newCategory.trim())
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

        // Dialog to Confirm Delete
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Category?") },
                text = { Text("Are you sure you want to delete \"$selectedCategory\" and all its tasks?") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.deleteCategory(selectedCategory)
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
