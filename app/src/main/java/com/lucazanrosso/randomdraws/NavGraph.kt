package com.lucazanrosso.randomdraws

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.lucazanrosso.randomdraws.ui.GroupDetailsDestination
import com.lucazanrosso.randomdraws.ui.GroupDetailsScreen
import com.lucazanrosso.randomdraws.ui.HomeDestination
import com.lucazanrosso.randomdraws.ui.HomeScreen
import com.lucazanrosso.randomdraws.ui.NewGroupDestination
import com.lucazanrosso.randomdraws.ui.NewGroupScreen

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
                navigateToNewGroup = { navController.navigate(NewGroupDestination.route)},
                navigateToGroupDetails = { navController.navigate("${GroupDetailsDestination.route}/${it}") }
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