package com.example.todoapp.repository

import com.example.todoapp.model.Category
import com.example.todoapp.model.Priority
import com.example.todoapp.model.SubTask
import com.example.todoapp.model.TodoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit

class TodoRepository {
    private val initialTodos = listOf(
        TodoItem(
            id = "1",
            title = "Implement MvRx State Management",
            description = "Connect Airbnb Mavericks ViewModel to Jetpack Compose UI components.",
            category = Category.WORK,
            priority = Priority.URGENT,
            isCompleted = true,
            subtasks = listOf(
                SubTask("s1", "Define MavericksState", true),
                SubTask("s2", "Implement MavericksViewModel", true),
                SubTask("s3", "Connect collectAsState in Compose", false)
            ),
            dueDateMillis = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)
        ),
        TodoItem(
            id = "2",
            title = "Design Glassmorphic Compose UI Theme",
            description = "Build vibrant dark theme colors, Material3 typography, and card components.",
            category = Category.WORK,
            priority = Priority.HIGH,
            isCompleted = false,
            subtasks = listOf(
                SubTask("s4", "Define dark palette", true),
                SubTask("s5", "Build Stats Overview Card", false)
            )
        ),
        TodoItem(
            id = "3",
            title = "Weekly Grocery List",
            description = "Oat milk, organic avocados, sourdough bread, espresso beans.",
            category = Category.SHOPPING,
            priority = Priority.MEDIUM,
            isCompleted = false
        ),
        TodoItem(
            id = "4",
            title = "Evening 5K Run",
            description = "Maintain pace under 5:00 min/km in the park.",
            category = Category.HEALTH,
            priority = Priority.LOW,
            isCompleted = false
        )
    )

    private val _todosFlow = MutableStateFlow<List<TodoItem>>(initialTodos)
    val todosFlow: Flow<List<TodoItem>> = _todosFlow.asStateFlow()

    fun getTodos(): Flow<List<TodoItem>> = _todosFlow

    fun addTodo(todo: TodoItem) {
        _todosFlow.value = listOf(todo) + _todosFlow.value
    }

    fun updateTodo(updated: TodoItem) {
        _todosFlow.value = _todosFlow.value.map { if (it.id == updated.id) updated else it }
    }

    fun toggleTodo(id: String) {
        _todosFlow.value = _todosFlow.value.map { item ->
            if (item.id == id) item.copy(isCompleted = !item.isCompleted) else item
        }
    }

    fun deleteTodo(id: String) {
        _todosFlow.value = _todosFlow.value.filterNot { it.id == id }
    }

    fun toggleSubtask(todoId: String, subtaskId: String) {
        _todosFlow.value = _todosFlow.value.map { todo ->
            if (todo.id == todoId) {
                val updatedSubtasks = todo.subtasks.map { subtask ->
                    if (subtask.id == subtaskId) subtask.copy(isCompleted = !subtask.isCompleted) else subtask
                }
                todo.copy(subtasks = updatedSubtasks)
            } else todo
        }
    }

    fun clearCompleted() {
        _todosFlow.value = _todosFlow.value.filterNot { it.isCompleted }
    }
}
