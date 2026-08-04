package com.example.todoapp.model

import java.util.UUID

data class SubTask(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val isCompleted: Boolean = false
)

data class TodoItem(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String = "",
    val category: Category = Category.PERSONAL,
    val priority: Priority = Priority.MEDIUM,
    val isCompleted: Boolean = false,
    val subtasks: List<SubTask> = emptyList(),
    val dueDateMillis: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)
