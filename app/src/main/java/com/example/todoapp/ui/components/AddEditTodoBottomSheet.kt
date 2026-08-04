package com.example.todoapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.todoapp.model.Category
import com.example.todoapp.model.Priority
import com.example.todoapp.model.TodoItem
import com.example.todoapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTodoBottomSheet(
    editingTodo: TodoItem?,
    onSave: (title: String, description: String, category: Category, priority: Priority, existingId: String?) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember(editingTodo) { mutableStateOf(editingTodo?.title ?: "") }
    var description by remember(editingTodo) { mutableStateOf(editingTodo?.description ?: "") }
    var selectedCategory by remember(editingTodo) { mutableStateOf(editingTodo?.category ?: Category.WORK) }
    var selectedPriority by remember(editingTodo) { mutableStateOf(editingTodo?.priority ?: Priority.MEDIUM) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = SurfaceDark,
        scrimColor = Color.Black.copy(alpha = 0.6f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = if (editingTodo != null) "Edit Task" else "Create New Task",
                style = MaterialTheme.typography.headlineMedium.copy(color = Color.White)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Title Field
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Task Title") },
                placeholder = { Text("What needs to be done?") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryNeon,
                    unfocusedBorderColor = BorderGlass,
                    focusedLabelColor = PrimaryNeon,
                    unfocusedLabelColor = Color(0xFF94A3B8),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(14.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Description Field
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description (Optional)") },
                placeholder = { Text("Add extra details or notes...") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryNeon,
                    unfocusedBorderColor = BorderGlass,
                    focusedLabelColor = PrimaryNeon,
                    unfocusedLabelColor = Color(0xFF94A3B8),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(14.dp),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Category Selection
            Text(
                text = "Select Category",
                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF94A3B8), fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(Category.assignableCategories()) { category ->
                    val isSelected = category == selectedCategory
                    val catColor = getCategoryColor(category)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) catColor.copy(alpha = 0.25f) else SurfaceVariantDark)
                            .border(1.dp, if (isSelected) catColor else BorderGlass, RoundedCornerShape(12.dp))
                            .clickable { selectedCategory = category }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = category.displayName,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = if (isSelected) Color.White else Color(0xFF94A3B8),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Priority Selection
            Text(
                text = "Select Priority",
                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF94A3B8), fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Priority.entries.forEach { priority ->
                    val isSelected = priority == selectedPriority
                    val prioColor = getPriorityColor(priority)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) prioColor.copy(alpha = 0.25f) else SurfaceVariantDark)
                            .border(1.dp, if (isSelected) prioColor else BorderGlass, RoundedCornerShape(12.dp))
                            .clickable { selectedPriority = priority }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = priority.label,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (isSelected) prioColor else Color(0xFF94A3B8),
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Save Button
            Button(
                onClick = { onSave(title, description, selectedCategory, selectedPriority, editingTodo?.id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = title.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryNeon),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = if (editingTodo != null) "Update Task" else "Create Task",
                    style = MaterialTheme.typography.titleLarge.copy(color = Color.White, fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}
