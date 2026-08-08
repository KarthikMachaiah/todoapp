package com.example.todoapp.mvrx

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.airbnb.mvrx.withState
import com.example.todoapp.model.Category
import com.example.todoapp.model.Priority
import com.example.todoapp.model.TodoItem
import com.example.todoapp.repository.TodoRepository

class TodoViewModel(
    initialState: TodoState,
    private val repository: TodoRepository = TodoRepository()
) : MavericksViewModel<TodoState>(initialState) {

    init {
        // Subscribe to repository flow using Mavericks execute helper
        repository.getTodos().execute { copy(todosAsync = it) }
    }

    fun setSelectedCategory(category: Category) {
        setState { copy(selectedCategory = category) }
    }

    fun setSelectedDate(date: java.time.LocalDate) {
        setState { copy(selectedDate = date) }
    }

    fun setSelectedPriority(priority: Priority?) {
        setState { copy(selectedPriority = priority) }
    }

    fun setSearchQuery(query: String) {
        setState { copy(searchQuery = query) }
    }

    fun openAddSheet(todoToEdit: TodoItem? = null) {
        setState { copy(isAddSheetOpen = true, editingTodo = todoToEdit) }
    }

    fun closeAddSheet() {
        setState { copy(isAddSheetOpen = false, editingTodo = null) }
    }

    fun toggleDarkMode() {
        setState { copy(isDarkMode = !isDarkMode) }
    }

    fun toggleTodo(id: String) {
        repository.toggleTodo(id)
    }

    fun deleteTodo(id: String) {
        repository.deleteTodo(id)
    }

    fun toggleSubtask(todoId: String, subtaskId: String) {
        repository.toggleSubtask(todoId, subtaskId)
    }

    fun saveTodo(
        title: String,
        description: String,
        category: Category,
        priority: Priority,
        existingId: String? = null
    ) {
        if (title.isBlank()) return
        if (existingId != null) {
            withState(this) { state ->
                val currentList = state.todosAsync() ?: emptyList()
                val existing = currentList.find { it.id == existingId }
                if (existing != null) {
                    val updated = existing.copy(
                        title = title.trim(),
                        description = description.trim(),
                        category = category,
                        priority = priority
                    )
                    repository.updateTodo(updated)
                }
            }
        } else {
            val newTodo = TodoItem(
                title = title.trim(),
                description = description.trim(),
                category = category,
                priority = priority
            )
            repository.addTodo(newTodo)
        }
        closeAddSheet()
    }

    fun clearCompleted() {
        repository.clearCompleted()
    }

    companion object : MavericksViewModelFactory<TodoViewModel, TodoState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: TodoState
        ): TodoViewModel {
            return TodoViewModel(state)
        }
    }
}
