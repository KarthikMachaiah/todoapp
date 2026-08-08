package com.example.todoapp.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todoapp.model.Priority
import com.example.todoapp.model.TodoItem
import com.example.todoapp.ui.theme.*

@Composable
fun TodoItemCard(
    todo: TodoItem,
    onToggleCompleted: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onToggleSubtask: (subtaskId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    val categoryColor = getCategoryColor(todo.category)
    val priorityColor = getPriorityColor(todo.priority)

    val cardBg by animateColorAsState(
        targetValue = if (todo.isCompleted) MaterialTheme.colorScheme.surface.copy(alpha = 0.6f) else MaterialTheme.colorScheme.surface,
        label = "bg"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, if (todo.isCompleted) MaterialTheme.colorScheme.outline.copy(alpha = 0.2f) else MaterialTheme.colorScheme.outline, RoundedCornerShape(20.dp))
            .clickable { isExpanded = !isExpanded },
        colors = CardDefaults.cardColors(containerColor = cardBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Checkbox toggle
                IconButton(
                    onClick = onToggleCompleted,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(
                            if (todo.isCompleted) SuccessGreen else MaterialTheme.colorScheme.surfaceVariant
                        )
                        .border(1.5.dp, if (todo.isCompleted) SuccessGreen else MaterialTheme.colorScheme.outline, CircleShape)
                ) {
                    if (todo.isCompleted) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Completed",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = todo.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = if (todo.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                            textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Category Tag
                        Surface(
                            color = categoryColor.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = todo.category.displayName,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = categoryColor,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }

                        // Priority Tag
                        Surface(
                            color = priorityColor.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = todo.priority.label,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = priorityColor,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }

                        if (todo.dueDateMillis != null) {
                            val formattedDate = java.time.Instant.ofEpochMilli(todo.dueDateMillis)
                                .atZone(java.time.ZoneId.systemDefault())
                                .toLocalDate()
                                .format(java.time.format.DateTimeFormatter.ofPattern("MMM dd"))
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarToday,
                                        contentDescription = "Due Date",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(10.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = formattedDate,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontWeight = FontWeight.Medium
                                        )
                                    )
                                }
                            }
                        }

                        if (todo.subtasks.isNotEmpty()) {
                            val doneCount = todo.subtasks.count { it.isCompleted }
                            Text(
                                text = "$doneCount/${todo.subtasks.size} subtasks",
                                style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }
                    }
                }

                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete",
                        tint = Color(0xFFF43F5E),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Expandable details section
            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {
                    if (todo.description.isNotBlank()) {
                        Text(
                            text = todo.description,
                            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    if (todo.subtasks.isNotEmpty()) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outline,
                            thickness = 0.5.dp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        Text(
                            text = "Checklist",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = PrimaryNeon,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            ),
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        todo.subtasks.forEach { subtask ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onToggleSubtask(subtask.id) }
                                    .padding(vertical = 4.dp)
                            ) {
                                Checkbox(
                                    checked = subtask.isCompleted,
                                    onCheckedChange = { onToggleSubtask(subtask.id) },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = PrimaryNeon,
                                        uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = subtask.title,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = if (subtask.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                                        textDecoration = if (subtask.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun getPriorityColor(priority: Priority): Color {
    return when (priority) {
        Priority.LOW -> PriorityLow
        Priority.MEDIUM -> PriorityMedium
        Priority.HIGH -> PriorityHigh
        Priority.URGENT -> PriorityUrgent
    }
}
