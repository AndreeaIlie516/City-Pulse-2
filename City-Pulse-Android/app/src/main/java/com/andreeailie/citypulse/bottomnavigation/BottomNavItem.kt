package com.andreeailie.citypulse.bottomnavigation

import com.andreeailie.citypulse.R

sealed class BottomNavItem(var title: String, var icon: Int, var screen_route: String) {
    object Home : BottomNavItem("Home", R.drawable.home_unselected_icon, "home")
    object Favorites : BottomNavItem("Favorites", R.drawable.favorite_unselected_icon, "favorites")
    object Profile : BottomNavItem("Profile", R.drawable.profile_unselected_icon, "profile")
}