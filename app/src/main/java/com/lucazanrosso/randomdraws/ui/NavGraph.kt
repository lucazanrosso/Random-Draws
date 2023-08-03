package com.lucazanrosso.randomdraws.ui

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lucazanrosso.randomdraws.R

sealed class Destination(val route: String, @StringRes val title: Int) {
    object Home : Destination("home", R.string.draws)
    object Groups : Destination("groups", R.string.groups)
    object NewGroup : Destination("new_group", R.string.new_group)
    object DrawDetail : Destination("draw_detail", R.string.draws)
}

@Composable
fun RandomDrawsNavHost(

    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = Destination.Home.route) {
        composable(Destination.Home.route) {
            HomeScreen(
                navigateToGroups = { navController.navigate(Destination.Groups.route) }
            )
        }

        composable(Destination.Groups.route) {
            GroupScreen(
                navigateToNewGroup = { navController.navigate(Destination.NewGroup.route)},
                navigateBack = { navController.navigateUp() },
            )
        }

        composable(Destination.NewGroup.route) {
            NewGroupScreen(navigateBack = {
                navController.navigateUp() }
            )
        }

    }
}