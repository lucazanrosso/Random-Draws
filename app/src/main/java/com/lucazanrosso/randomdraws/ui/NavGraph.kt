package com.lucazanrosso.randomdraws.ui

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lucazanrosso.randomdraws.R

sealed class Screen(val route: String, @StringRes val title: Int) {
    object Home : Screen("all_draws", R.string.draws)
    object AllGroups : Screen("all_groups", R.string.groups)
    object NewGroup : Screen("new_group", R.string.new_group)
    object DrawsDetails : Screen("draws_detail", R.string.draws)
}

@Composable
fun RandomDrawsNavHost(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(navigateToNewGroup = {navController.navigate(Screen.NewGroup.route)})
        }

        composable(Screen.AllGroups.route) {
            GroupScreen()
        }
        composable(Screen.NewGroup.route) {
            NewGroupScreen(navigateBack = { navController.popBackStack() })
        }

    }
}