package com.example.todoapp.mvrx

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.example.todoapp.model.Category
import com.example.todoapp.model.Priority
import com.example.todoapp.model.TodoItem

data class TodoState(
    val todosAsync: Async<List<TodoItem>> = Uninitialized,
    val selectedCategory: Category = Category.ALL,
    val selectedPriority: Priority? = null,
    val searchQuery: String = "",
    val isAddSheetOpen: Boolean = false,
    val editingTodo: TodoItem? = null
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
                matchesCategory && matchesPriority && matchesSearch
            }
        }

    val totalCount: Int
        get() = (todosAsync() ?: emptyList()).size

    val completedCount: Int
        get() = (todosAsync() ?: emptyList()).count { it.isCompleted }

    val completionPercentage: Float
        get() = if (totalCount == 0) 0f else completedCount.toFloat() / totalCount.toFloat()
}
