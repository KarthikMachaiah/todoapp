package com.example.todoapp.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todoapp.ui.theme.PrimaryNeon
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeeklyDateStrip(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val initialPage = 1000 // Infinite swipe center page
    val pagerState = rememberPagerState(initialPage = initialPage, pageCount = { 2000 })
    val coroutineScope = rememberCoroutineScope()

    // Calculate start date of week for a given pager page
    fun getWeekStartDateForPage(page: Int): LocalDate {
        val weekOffset = page - initialPage
        val today = LocalDate.now()
        val mondayThisWeek = today.with(DayOfWeek.MONDAY)
        return mondayThisWeek.plusWeeks(weekOffset.toLong())
    }

    val currentWeekStart = getWeekStartDateForPage(pagerState.currentPage)
    val monthYearText = currentWeekStart.format(DateTimeFormatter.ofPattern("MMMM yyyy"))

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp)
    ) {
        // Month & Navigation Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = monthYearText,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    onClick = {
                        val today = LocalDate.now()
                        onDateSelected(today)
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(initialPage)
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    color = PrimaryNeon.copy(alpha = 0.15f),
                    modifier = Modifier.padding(end = 4.dp)
                ) {
                    Text(
                        text = "Today",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = PrimaryNeon,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronLeft,
                        contentDescription = "Previous Week",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Next Week",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // District/Playo Swipeable Weekly Strip Pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val weekStart = getWeekStartDateForPage(page)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                (0..6).forEach { dayIndex ->
                    val date = weekStart.plusDays(dayIndex.toLong())
                    val isSelected = date == selectedDate
                    val isToday = date == LocalDate.now()

                    val bgColor by animateColorAsState(
                        targetValue = when {
                            isSelected -> PrimaryNeon
                            isToday -> PrimaryNeon.copy(alpha = 0.15f)
                            else -> Color.Transparent
                        },
                        animationSpec = tween(300, easing = FastOutSlowInEasing),
                        label = "bgColor"
                    )

                    val textColor by animateColorAsState(
                        targetValue = when {
                            isSelected -> Color.White
                            isToday -> PrimaryNeon
                            else -> MaterialTheme.colorScheme.onSurface
                        },
                        animationSpec = tween(300, easing = FastOutSlowInEasing),
                        label = "textColor"
                    )

                    val dayNameColor by animateColorAsState(
                        targetValue = when {
                            isSelected -> Color.White.copy(alpha = 0.9f)
                            isToday -> PrimaryNeon
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        animationSpec = tween(300, easing = FastOutSlowInEasing),
                        label = "dayNameColor"
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 2.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(bgColor)
                            .border(
                                width = if (isToday && !isSelected) 1.5.dp else 0.dp,
                                color = if (isToday && !isSelected) PrimaryNeon else Color.Transparent,
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clickable { onDateSelected(date) }
                            .padding(vertical = 10.dp)
                    ) {
                        Text(
                            text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()).uppercase(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = dayNameColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = date.dayOfMonth.toString(),
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = textColor,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp
                            )
                        )
                    }
                }
            }
        }
    }
}
