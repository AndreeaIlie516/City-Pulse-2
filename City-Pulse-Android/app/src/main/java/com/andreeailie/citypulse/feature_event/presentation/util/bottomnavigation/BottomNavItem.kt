package com.andreeailie.citypulse.feature_event.presentation.util.bottomnavigation

import com.andreeailie.citypulse.R

sealed class BottomNavItem(var title: String, var icon: Int, var screenRoute: String) {
    data object Home : BottomNavItem("Home", R.drawable.home_unselected_icon, "home")
    data object Favourites : BottomNavItem("Favourites", R.drawable.favorite_unselected_icon, "favorites")
    data object Profile : BottomNavItem("Profile", R.drawable.profile_unselected_icon, "profile")
}