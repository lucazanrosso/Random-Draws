package com.lucazanrosso.randomdraws

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.lucazanrosso.randomdraws.ui.DrawDestination
import com.lucazanrosso.randomdraws.ui.DrawScreen
import com.lucazanrosso.randomdraws.ui.EditGroupDestination
import com.lucazanrosso.randomdraws.ui.EditGroupScreen
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
                navigateToDraw = { navController.navigate("${DrawDestination.route}/${it}") },
                navigateToEditGroup = { navController.navigate("${EditGroupDestination.route}/${it}") }
            )
        }

        composable(NewGroupDestination.route) {
            NewGroupScreen(navigateBack = {
                navController.navigateUp() }
            )
        }

        composable(
            route = DrawDestination.routeWithArgs,
            arguments = listOf(navArgument(DrawDestination.itemIdArg) {
                type = NavType.StringType
            })
        ) {
            DrawScreen(
                navigateBack = { navController.navigateUp() }
            )
        }

        composable(
            route = EditGroupDestination.routeWithArgs,
            arguments = listOf(navArgument(EditGroupDestination.itemIdArg) {
                type = NavType.StringType
            })
        ) {
            EditGroupScreen(
                navigateBack = { navController.navigateUp() }
            )
        }

    }
}