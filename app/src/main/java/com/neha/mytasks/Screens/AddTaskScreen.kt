// --- AddTaskScreen.kt (Updated Beginner-Friendly Version)
package com.neha.mytasks.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    category: String,
    onAddClick: (title: String, description: String, priority: String) -> Unit,
    onBackClick: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("MEDIUM") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Task") },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (title.isNotBlank() && description.isNotBlank()) {
                    onAddClick(title, description, priority)
                }
            }) {
                Text("Save")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Category: $category", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                Text("Priority:", fontWeight = FontWeight.SemiBold)
                Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                    listOf("HIGH", "MEDIUM", "LOW").forEach { level ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = priority == level,
                                onClick = { priority = level }
                            )
                            Text(text = level)
                        }
                    }
                }
            }
        }
    }
}
