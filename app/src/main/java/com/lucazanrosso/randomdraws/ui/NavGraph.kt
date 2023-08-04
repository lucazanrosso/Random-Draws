package com.lucazanrosso.randomdraws.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

interface NavigationDestination {
    /**
     * Unique name to define the path for a composable
     */
    val route: String

    /**
     * String resource id to that contains title to be displayed for the screen.
     */
    val titleRes: Int
}

@Composable
fun RandomDrawsNavHost(

    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = HomeDestination.route) {
        composable(HomeDestination.route) {
            HomeScreen(
                navigateToGroups = { navController.navigate(GroupsDestination.route) }
            )
        }

        composable(GroupsDestination.route) {
            GroupScreen(
                navigateToNewGroup = { navController.navigate(NewGroupDestination.route)},
                navigateBack = { navController.navigateUp() },
                navigateToGroupDetails = { "${GroupDetailsDestination.route}/${it}"}
            )
        }

        composable(NewGroupDestination.route) {
            NewGroupScreen(navigateBack = {
                navController.navigateUp() }
            )
        }

        composable(
            route = GroupDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(GroupDetailsDestination.itemIdArg) {
                type = NavType.StringType
            })
        ) {
            GroupDetailsScreen(
                navigateBack = { navController.navigateUp() }
            )
        }

    }
}