package com.example.todoapp.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.example.todoapp.mvrx.TodoViewModel
import com.example.todoapp.ui.components.*
import com.example.todoapp.ui.theme.BackgroundDark
import com.example.todoapp.ui.theme.PrimaryNeon
import com.example.todoapp.ui.theme.SecondaryCyan
import com.example.todoapp.ui.theme.TodoAppTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(
    viewModel: TodoViewModel = mavericksViewModel()
) {
    val state by viewModel.collectAsState()
    var isSplashVisible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(1200)
        isSplashVisible = false
    }

    TodoAppTheme(darkTheme = state.isDarkMode) {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                containerColor = MaterialTheme.colorScheme.background,
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
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "MvRx Jetpack Compose Architecture",
                                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }

                        IconButton(onClick = { viewModel.toggleDarkMode() }) {
                            val iconScale by animateFloatAsState(
                                targetValue = if (state.isDarkMode) 1.2f else 1.0f,
                                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                                label = "iconScale"
                            )
                            Icon(
                                imageVector = if (state.isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Toggle Theme",
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.scale(iconScale)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Search Bar
                    OutlinedTextField(
                        value = state.searchQuery,
                        onValueChange = { viewModel.setSearchQuery(it) },
                        placeholder = { Text("Search tasks...", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedBorderColor = PrimaryNeon,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // District / Playo Style Swipeable Weekly Date Strip
                    WeeklyDateStrip(
                        selectedDate = state.selectedDate,
                        onDateSelected = { viewModel.setSelectedDate(it) }
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

                    // Todo List Section with Zomato-style Animated Transitions
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
                                    style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                                Text(
                                    text = "Tap + to add a new task",
                                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f))
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

            // Zomato-Style Animated Splash Screen Overlay
            AnimatedVisibility(
                visible = isSplashVisible,
                exit = slideOutVertically(
                    targetOffsetY = { -it },
                    animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
                ) + fadeOut(animationSpec = tween(durationMillis = 600))
            ) {
                val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                val pulseScale by infiniteTransition.animateFloat(
                    initialValue = 0.92f,
                    targetValue = 1.08f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(600, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "scale"
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    BackgroundDark,
                                    Color(0xFF1E1B4B),
                                    BackgroundDark
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = PrimaryNeon.copy(alpha = 0.2f),
                            modifier = Modifier
                                .size(110.dp)
                                .scale(pulseScale)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Logo",
                                    tint = PrimaryNeon,
                                    modifier = Modifier.size(64.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "MvRx Todo",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.sp
                            )
                        )
                        Text(
                            text = "Productivity Flow • Zomato Design Motion",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = SecondaryCyan,
                                fontWeight = FontWeight.Medium
                            )
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        CircularProgressIndicator(
                            color = PrimaryNeon,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    }
}
