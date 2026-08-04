package com.example.todoapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.todoapp.model.Category
import com.example.todoapp.ui.theme.*

@Composable
fun CategoryChipGroup(
    selectedCategory: Category,
    onCategorySelected: (Category) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(Category.filterableCategories()) { category ->
            val isSelected = category == selectedCategory
            val categoryColor = getCategoryColor(category)
            val icon = getCategoryIcon(category)

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (isSelected) categoryColor.copy(alpha = 0.25f) else SurfaceVariantDark.copy(alpha = 0.5f)
                    )
                    .border(
                        width = 1.dp,
                        color = if (isSelected) categoryColor else BorderGlass,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable { onCategorySelected(category) }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = category.displayName,
                        tint = if (isSelected) categoryColor else Color(0xFF94A3B8),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = category.displayName,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = if (isSelected) Color.White else Color(0xFF94A3B8)
                        )
                    )
                }
            }
        }
    }
}

fun getCategoryColor(category: Category): Color {
    return when (category) {
        Category.ALL -> PrimaryNeon
        Category.WORK -> ColorWork
        Category.PERSONAL -> ColorPersonal
        Category.SHOPPING -> ColorShopping
        Category.HEALTH -> ColorHealth
        Category.FINANCE -> ColorFinance
    }
}

fun getCategoryIcon(category: Category): ImageVector {
    return when (category) {
        Category.ALL -> Icons.Default.ListAlt
        Category.WORK -> Icons.Default.Work
        Category.PERSONAL -> Icons.Default.Person
        Category.SHOPPING -> Icons.Default.ShoppingCart
        Category.HEALTH -> Icons.Default.Favorite
        Category.FINANCE -> Icons.Default.AttachMoney
    }
}
