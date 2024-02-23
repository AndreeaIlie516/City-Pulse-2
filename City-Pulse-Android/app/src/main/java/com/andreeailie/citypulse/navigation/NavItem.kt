package com.andreeailie.citypulse.navigation

sealed class NavItem(var title: String, var screen_route: String) {
    object Add : NavItem("Add", "add")

}