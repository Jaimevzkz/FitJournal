package com.vzkz.fitjournal.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kizitonwose.calendar.compose.CalendarLayoutInfo
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.vzkz.fitjournal.core.boilerplate.USERMODELFORTESTS
import com.vzkz.fitjournal.destinations.HomeScreenDestination
import com.vzkz.fitjournal.destinations.ProfileScreenDestination
import com.vzkz.fitjournal.domain.model.Constants.ERRORSTR
import com.vzkz.fitjournal.domain.model.WorkoutModel
import com.vzkz.fitjournal.ui.components.MySpacer
import com.vzkz.fitjournal.ui.components.bottombar.MyBottomBar
import com.vzkz.fitjournal.ui.theme.FitJournalTheme
import com.vzkz.fitjournal.ui.theme.selectedDate
import com.vzkz.fitjournal.ui.theme.workOutedColor
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Destination
@Composable
fun HomeScreen(navigator: DestinationsNavigator, homeViewModel: HomeViewModel = hiltViewModel()) {
    homeViewModel.onInit()
    var wotDates by remember { mutableStateOf<List<Pair<LocalDate, String>>?>(emptyList()) }
    wotDates = homeViewModel.state.user?.wotDates ?: emptyList()
    var wotList: List<WorkoutModel> by remember { mutableStateOf(emptyList()) }
    wotList = homeViewModel.state.user?.workouts ?: emptyList()


    if (homeViewModel.state.start) {
        homeViewModel.onSelectedDate(LocalDate.now(), wotDates ?: emptyList(), wotList)
        var workout: WorkoutModel by remember { mutableStateOf(homeViewModel.state.workout) }

        ScreenBody(
            workout = workout,
            wotDates = wotDates ?: emptyList(),
            onSelectedDate = {
                homeViewModel.onSelectedDate(it, wotDates ?: emptyList(), wotList)
                workout = homeViewModel.state.workout
            },
            onBottomBarClicked = { navigator.navigate(it) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenBody(
    workout: WorkoutModel,
    wotDates: List<Pair<LocalDate, String>>,
    onSelectedDate: (LocalDate?) -> Unit,
    onBottomBarClicked: (DirectionDestinationSpec) -> Unit
) {
    Scaffold(bottomBar = {
        MyBottomBar(
            currentDestination = HomeScreenDestination,
            onClick = { if(it != HomeScreenDestination) onBottomBarClicked(it) })
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .padding(top = 12.dp)
                    .align(Alignment.TopCenter)
                    .verticalScroll(rememberScrollState())
            ) {
                val currentMonth = remember { YearMonth.now() }
                val currentDate = remember { LocalDate.now() }
                val startMonth = remember { currentMonth.minusMonths(100) }
                val endMonth = remember { currentMonth.plusMonths(100) }
                val daysOfWeek = remember { daysOfWeek() }
                val state = rememberCalendarState(
                    startMonth = startMonth,
                    endMonth = endMonth,
                    firstVisibleMonth = currentMonth,
                    firstDayOfWeek = daysOfWeek.first()
                )
                val coroutineScope = rememberCoroutineScope()
                val visibleMonth = rememberFirstMostVisibleMonth(state, viewportPercent = 90f)

                var selectedDate by remember { mutableStateOf<LocalDate?>(null) }


                Column(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    HorizontalCalendar(
                        state = state,
                        userScrollEnabled = false,
                        dayContent = { day ->
                            Day(
                                day = day,
                                currentDate = currentDate,
                                isSelected = selectedDate == day.date,
                                haveWorkout = wotDates.any{it.first == day.date},
                                onClick = { dayClicked ->
                                    selectedDate =
                                        if (selectedDate == dayClicked.date) null else dayClicked.date
                                    onSelectedDate(selectedDate)
                                }
                            )
                        },
                        monthHeader = {
                            CalendarHeader(
                                currentMonth = visibleMonth.yearMonth,
                                daysOfWeek = daysOfWeek,
                                onPreviousMonth = {
                                    coroutineScope.launch {
                                        state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.previousMonth)
                                    }
                                },
                                onNextMonth = {
                                    coroutineScope.launch {
                                        state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.nextMonth)
                                    }
                                },
                                onToday = {
                                    coroutineScope.launch {
                                        state.animateScrollToMonth(currentMonth)
                                    }
                                }
                            )
                        }
                    )
                }
                MySpacer(size = 16)

                if (selectedDate != null) {
                    MyWorkoutSumCard(
                        date = selectedDate!!,
                        wot = workout,
                        today = selectedDate == currentDate
                    )
                } else {
                    MyWorkoutSumCard(
                        date = currentDate,
                        today = true,
                        wot = workout
                    )
                }
            }
        }
    }
}

@Composable
fun Day(
    day: CalendarDay,
    isSelected: Boolean,
    haveWorkout: Boolean,
    onClick: (CalendarDay) -> Unit,
    currentDate: LocalDate
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(4.dp)
            .clip(CircleShape)
            .background(
                color =
                if (isSelected) selectedDate
                else if (haveWorkout) workOutedColor
                else Color.Transparent,
            )
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = { onClick(day) }
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = day.date.dayOfMonth.toString(),
                color = if (day.position == DayPosition.MonthDate) MaterialTheme.colorScheme.onPrimaryContainer else Color.Gray
            )

            if (currentDate == day.date && day.position == DayPosition.MonthDate) {
                Icon(
                    imageVector = Icons.Filled.Circle,
                    contentDescription = "today",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            } else {
                Text(
                    text = "",
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 40.sp
                )
            }
        }

    }
}


@Composable
fun CalendarHeader(
    daysOfWeek: List<DayOfWeek>,
    currentMonth: YearMonth,
    onNextMonth: () -> Unit,
    onToday: () -> Unit,
    onPreviousMonth: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            IconButton(onClick = { onPreviousMonth() }) {
                Icon(imageVector = Icons.Filled.ArrowBackIos, contentDescription = "Previous Month")
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = currentMonth.month.getDisplayName(
                    TextStyle.SHORT,
                    Locale.ENGLISH
                ) + " " + currentMonth.year,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(0.4f))
            IconButton(onClick = { onToday() }) {
                Icon(imageVector = Icons.Filled.Today, contentDescription = "Go to this month")
            }

            Spacer(modifier = Modifier.weight(0.4f))
            IconButton(onClick = { onNextMonth() }) {
                Icon(imageVector = Icons.Filled.ArrowForwardIos, contentDescription = "Next Month")
            }
        }
        MySpacer(size = 8)
        Row(modifier = Modifier.fillMaxWidth()) {
            for (dayOfWeek in daysOfWeek) {
                Text(
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    text = dayOfWeek.getDisplayName(
                        TextStyle.SHORT,
                        Locale.ENGLISH
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
private fun MyWorkoutSumCard(
    date: LocalDate = LocalDate.of(2023, 12, 4),
    wot: WorkoutModel,
    today: Boolean = false,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.secondaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (today) {
                    "Today"
                } else {
                    "${date.dayOfMonth}-${date.monthValue}-${date.year}"
                } + "'s workout",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            if (wot.wid != ERRORSTR) {
                Text(
                    text = wot.wotName,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.titleLarge
                )
                MySpacer(size = 4)
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = ("${wot.duration} min"),
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    MySpacer(size = 12)
                    Text(
                        text = "${wot.exercises.size} exercises",
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }else{
                MySpacer(size = 8)
                Text(
                    text = "No workout logs for this day...",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.titleLarge
                )
                MySpacer(size = 8)
            }
        }
    }
}



@Composable
fun rememberFirstMostVisibleMonth(
    state: CalendarState,
    viewportPercent: Float = 50f,
): CalendarMonth {
    val visibleMonth = remember(state) { mutableStateOf(state.firstVisibleMonth) }
    LaunchedEffect(state) {
        snapshotFlow { state.layoutInfo.firstMostVisibleMonth(viewportPercent) }
            .filterNotNull()
            .collect { month -> visibleMonth.value = month }
    }
    return visibleMonth.value
}

private fun CalendarLayoutInfo.firstMostVisibleMonth(viewportPercent: Float = 50f): CalendarMonth? {
    return if (visibleMonthsInfo.isEmpty()) {
        null
    } else {
        val viewportSize = (viewportEndOffset + viewportStartOffset) * viewportPercent / 100f
        visibleMonthsInfo.firstOrNull { itemInfo ->
            if (itemInfo.offset < 0) {
                itemInfo.offset + itemInfo.size >= viewportSize
            } else {
                itemInfo.size - itemInfo.offset >= viewportSize
            }
        }?.month
    }
}

@Preview
//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark preview")
@Composable
fun LightPreview() {
    FitJournalTheme {
        ScreenBody(
            onBottomBarClicked = {},
            onSelectedDate = {},
            wotDates = USERMODELFORTESTS.wotDates,
            workout = USERMODELFORTESTS.workouts?.get(0) ?: WorkoutModel(),
        )
    }
}
