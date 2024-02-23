package com.andreeailie.citypulse.bottomnavigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.andreeailie.citypulse.favoriteevents.FavoriteEventsScreen
import com.andreeailie.citypulse.popularevents.PopularEventsScreen
import com.andreeailie.citypulse.profile.ProfileScreen

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = BottomNavItem.Home.screen_route) {
        composable(BottomNavItem.Home.screen_route) {
            PopularEventsScreen()
        }
        composable(BottomNavItem.Favorites.screen_route) {
            FavoriteEventsScreen()
        }
        composable(BottomNavItem.Profile.screen_route) {
            ProfileScreen()
        }
    }
}