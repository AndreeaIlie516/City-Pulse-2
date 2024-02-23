package com.andreeailie.citypulse.feature_event.presentation.util.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.andreeailie.citypulse.feature_event.presentation.add_edit_event.AddEditEventScreen
import com.andreeailie.citypulse.feature_event.presentation.events.PopularEventsScreen
import com.andreeailie.citypulse.feature_event.presentation.favourite_events.FavoriteEventsScreen
import com.andreeailie.citypulse.feature_event.presentation.profile.ProfileScreen
import com.andreeailie.citypulse.feature_event.presentation.util.bottomnavigation.BottomNavItem

@Composable
fun NavigationGraph(
    navController: NavHostController,
) {
    NavHost(navController, startDestination = BottomNavItem.Home.screenRoute) {
        composable(BottomNavItem.Home.screenRoute) {
            PopularEventsScreen()
        }
        composable(BottomNavItem.Favourites.screenRoute) {
            FavoriteEventsScreen(navController)
        }
        composable(BottomNavItem.Profile.screenRoute) {
            ProfileScreen()
        }
        composable(
            route = NavItem.Add.screenRoute + "?eventId={eventId}",
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