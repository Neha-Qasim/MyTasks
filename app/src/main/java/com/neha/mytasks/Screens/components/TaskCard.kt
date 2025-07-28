package com.neha.mytasks.Screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neha.mytasks.model.Task

@Composable
fun TaskCard(task: Task, onClick: () -> Unit) {
    val bgColor = when (task.priority.uppercase()) {
        "HIGH" -> Color(0xFFA5D6A7)
        "MEDIUM" -> Color(0xFFFFF59D)
        else -> Color(0xFFF8BBD0)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                task.title,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                task.description,
                fontSize = 14.sp,
                color = Color.DarkGray
            )
        }
    }
}
