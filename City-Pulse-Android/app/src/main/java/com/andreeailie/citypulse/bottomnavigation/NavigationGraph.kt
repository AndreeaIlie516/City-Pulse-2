package com.andreeailie.citypulse.bottomnavigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.andreeailie.citypulse.events.EventViewModel
import com.andreeailie.citypulse.favoriteevents.FavoriteEventsScreen
import com.andreeailie.citypulse.popularevents.PopularEventsScreen
import com.andreeailie.citypulse.profile.ProfileScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    eventViewModel: EventViewModel
) {
    NavHost(navController, startDestination = BottomNavItem.Home.screen_route) {
        composable(BottomNavItem.Home.screen_route) {
            PopularEventsScreen(eventViewModel = eventViewModel)
        }
        composable(BottomNavItem.Favorites.screen_route) {
            FavoriteEventsScreen(eventViewModel = eventViewModel)
        }
        composable(BottomNavItem.Profile.screen_route) {
            ProfileScreen()
        }
    }
}