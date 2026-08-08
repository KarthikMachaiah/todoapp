package com.example.todoapp.mvrx

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.example.todoapp.model.Category
import com.example.todoapp.model.Priority
import com.example.todoapp.model.TodoItem

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class TodoState(
    val todosAsync: Async<List<TodoItem>> = Uninitialized,
    val selectedCategory: Category = Category.ALL,
    val selectedPriority: Priority? = null,
    val selectedDate: LocalDate = LocalDate.now(),
    val searchQuery: String = "",
    val isAddSheetOpen: Boolean = false,
    val editingTodo: TodoItem? = null,
    val isDarkMode: Boolean = true
) : MavericksState {

    // Derived property for filtered todos
    val filteredTodos: List<TodoItem>
        get() {
            val list = todosAsync() ?: emptyList()
            return list.filter { todo ->
                val matchesCategory = (selectedCategory == Category.ALL || todo.category == selectedCategory)
                val matchesPriority = (selectedPriority == null || todo.priority == selectedPriority)
                val matchesSearch = searchQuery.isBlank() ||
                        todo.title.contains(searchQuery, ignoreCase = true) ||
                        todo.description.contains(searchQuery, ignoreCase = true)
                
                val matchesDate = if (todo.dueDateMillis != null) {
                    val todoDate = Instant.ofEpochMilli(todo.dueDateMillis)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    todoDate == selectedDate
                } else {
                    true // Show all tasks if date filter is reset or unset
                }

                matchesCategory && matchesPriority && matchesSearch && matchesDate
            }
        }

    val totalCount: Int
        get() = (todosAsync() ?: emptyList()).size

    val completedCount: Int
        get() = (todosAsync() ?: emptyList()).count { it.isCompleted }

    val completionPercentage: Float
        get() = if (totalCount == 0) 0f else completedCount.toFloat() / totalCount.toFloat()
}
