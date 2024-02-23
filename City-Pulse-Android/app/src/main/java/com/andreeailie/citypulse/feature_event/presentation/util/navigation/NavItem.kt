package com.andreeailie.citypulse.feature_event.presentation.util.navigation

sealed class NavItem(var title: String, var screenRoute: String) {
    data object Add : NavItem("AddEdit", "add_edit_event_screen")

}