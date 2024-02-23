package com.andreeailie.citypulse.feature_event.presentation.util

sealed class Screen(val route: String) {
    data object PopularEventsScreen : Screen("popular_events_screen")
    data object FavouriteEventsScreen : Screen("favourite_events_screen")
    data object AddEditEventScreen : Screen("add_edit_event_screen")
}