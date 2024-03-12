package com.andreeailie.citypulse.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.andreeailie.event_presentation.add_edit_event.AddEditEventScreen
import com.andreeailie.event_presentation.popular_events_screen.PopularEventsScreen
import com.andreeailie.event_presentation.favourite_events_screen.FavoriteEventsScreen
import com.andreeailie.event_presentation.profile_screen.ProfileScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
) {
    NavHost(navController, startDestination = com.andreeailie.core.navigation.BottomNavItem.Home.screenRoute) {
        composable(com.andreeailie.core.navigation.BottomNavItem.Home.screenRoute) {
            PopularEventsScreen()
        }
        composable(com.andreeailie.core.navigation.BottomNavItem.Favourites.screenRoute) {
            FavoriteEventsScreen(navController)
        }
        composable(com.andreeailie.core.navigation.BottomNavItem.Profile.screenRoute) {
            ProfileScreen()
        }
        composable(
            route = com.andreeailie.core.navigation.NavItem.Add.screenRoute + "?eventId={eventId}",
            arguments = listOf(
                navArgument(
                    name = "eventId"
                ) {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) {
            AddEditEventScreen(navController = navController)
        }
    }
}