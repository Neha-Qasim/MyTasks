// --- TaskListScreen.kt (Fully Updated)
package com.neha.mytasks.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neha.mytasks.model.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    category: String,
    tasks: List<Task>,
    onBackClick: () -> Unit,
    onTaskClick: (Task) -> Unit,
    onCheckComplete: (Task, Boolean) -> Unit,
    onAddClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Tasks") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Task")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(tasks.sortedBy { it.priority }) { task ->
                var checked by remember { mutableStateOf(task.isCompleted) }
                val bgColor = when (task.priority.uppercase()) {
                    "HIGH" -> Color(0xFFA5D6A7)
                    "MEDIUM" -> Color(0xFFFFFF8D)
                    else -> Color(0xFFF8BBD0)
                }
                Card(
                    colors = CardDefaults.cardColors(containerColor = bgColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onTaskClick(task) }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Checkbox(
                            checked = checked,
                            onCheckedChange = {
                                checked = it
                                onCheckComplete(task, it)
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = task.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = if (checked) Color.Gray else Color.Black
                            )
                            Text(
                                text = task.description,
                                fontSize = 14.sp,
                                color = Color.DarkGray
                            )
                        }
                    }
                }
            }
        }
    }
}
