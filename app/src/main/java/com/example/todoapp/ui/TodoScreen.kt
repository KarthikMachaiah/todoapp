package com.example.todoapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.example.todoapp.mvrx.TodoViewModel
import com.example.todoapp.ui.components.*
import com.example.todoapp.ui.theme.BackgroundDark
import com.example.todoapp.ui.theme.BorderGlass
import com.example.todoapp.ui.theme.PrimaryNeon
import com.example.todoapp.ui.theme.SurfaceDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(
    viewModel: TodoViewModel = mavericksViewModel()
) {
    val state by viewModel.collectAsState()

    Scaffold(
        containerColor = BackgroundDark,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.openAddSheet() },
                containerColor = PrimaryNeon,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(60.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Task",
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Header Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "My Tasks",
                        style = MaterialTheme.typography.headlineLarge.copy(color = Color.White)
                    )
                    Text(
                        text = "MvRx Jetpack Compose Architecture",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF94A3B8))
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = { Text("Search tasks...", color = Color(0xFF64748B)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color(0xFF94A3B8)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SurfaceDark,
                    unfocusedContainerColor = SurfaceDark,
                    focusedBorderColor = PrimaryNeon,
                    unfocusedBorderColor = BorderGlass,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Productivity Stats Overview Card
            StatsCard(
                totalCount = state.totalCount,
                completedCount = state.completedCount,
                percentage = state.completionPercentage,
                onClearCompleted = { viewModel.clearCompleted() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Category Chips
            CategoryChipGroup(
                selectedCategory = state.selectedCategory,
                onCategorySelected = { viewModel.setSelectedCategory(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Todo List Section
            val todos = state.filteredTodos
            if (todos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "No tasks found",
                            style = MaterialTheme.typography.titleLarge.copy(color = Color(0xFF64748B))
                        )
                        Text(
                            text = "Tap + to add a new task",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF475569))
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(todos, key = { it.id }) { todo ->
                        TodoItemCard(
                            todo = todo,
                            onToggleCompleted = { viewModel.toggleTodo(todo.id) },
                            onDelete = { viewModel.deleteTodo(todo.id) },
                            onEdit = { viewModel.openAddSheet(todo) },
                            onToggleSubtask = { subtaskId -> viewModel.toggleSubtask(todo.id, subtaskId) }
                        )
                    }
                }
            }
        }

        // Add / Edit Modal Bottom Sheet
        if (state.isAddSheetOpen) {
            AddEditTodoBottomSheet(
                editingTodo = state.editingTodo,
                onSave = { title, description, category, priority, existingId ->
                    viewModel.saveTodo(title, description, category, priority, existingId)
                },
                onDismiss = { viewModel.closeAddSheet() }
            )
        }
    }
}
