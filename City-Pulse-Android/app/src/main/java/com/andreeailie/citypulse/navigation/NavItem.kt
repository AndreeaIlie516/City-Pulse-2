package com.andreeailie.citypulse.navigation

sealed class NavItem(var title: String, var screenRoute: String) {
    data object Add : NavItem("Add", "add")
    data object Update : NavItem("Update", "update")
}