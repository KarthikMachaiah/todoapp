package com.example.todoapp.model

enum class Category(val displayName: String, val iconName: String) {
    ALL("All Tasks", "ListAlt"),
    WORK("Work", "Work"),
    PERSONAL("Personal", "Person"),
    SHOPPING("Shopping", "ShoppingCart"),
    HEALTH("Health", "Favorite"),
    FINANCE("Finance", "AttachMoney");

    companion object {
        fun filterableCategories(): List<Category> = entries.toList()
        fun assignableCategories(): List<Category> = entries.filter { it != ALL }
    }
}
