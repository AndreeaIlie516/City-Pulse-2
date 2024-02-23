package com.andreeailie.citypulse.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.andreeailie.citypulse.bottomnavigation.BottomNavItem
import com.andreeailie.citypulse.events.EventViewModel
import com.andreeailie.citypulse.favoriteevents.FavoriteEventsScreen
import com.andreeailie.citypulse.popularevents.PopularEventsScreen
import com.andreeailie.citypulse.privateevent.AddPrivateEventScreen
import com.andreeailie.citypulse.profile.ProfileScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    eventViewModel: EventViewModel
) {
    NavHost(navController, startDestination = BottomNavItem.Home.screenRoute) {
        composable(BottomNavItem.Home.screenRoute) {
            PopularEventsScreen(eventViewModel = eventViewModel)
        }
        composable(BottomNavItem.Favorites.screenRoute) {
            FavoriteEventsScreen(eventViewModel = eventViewModel, navController = navController)
        }
        composable(BottomNavItem.Profile.screenRoute) {
            ProfileScreen()
        }
        composable(NavItem.Add.screenRoute) {
            AddPrivateEventScreen(navController = navController, eventViewModel = eventViewModel)
        }
    }
}