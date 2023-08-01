package com.lucazanrosso.randomdraws.ui

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.lucazanrosso.randomdraws.R
import com.lucazanrosso.randomdraws.data.AppDatabase

/*enum class Screen(@StringRes val title: Int, val icon: ImageVector) {
    Groups(R.string.groups, Icons.Rounded.Home),
    Draws(R.string.questions, Icons.Rounded.Settings)
}*/

sealed class Root(val route: String, @StringRes val title: Int, val icon: ImageVector) {
    object Groups : Root("groups", R.string.groups, Icons.Rounded.Home)
    object Draws : Root("draws", R.string.draws, Icons.Rounded.List)
}

sealed class Screen(val route: String, @StringRes val title: Int) {
    object AllGroups : Screen("all_groups", R.string.groups)
    object NewGroup : Screen("new_group", R.string.new_group)
    object AllDraws : Screen("all_draws", R.string.draws)
}

val allScreens = listOf(Screen.AllGroups, Screen.NewGroup, Screen.AllDraws)
val testScreen = listOf(Screen.NewGroup)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SampleApp() {

    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
//    var currentScreen = Screen.valueOf( backStackEntry?.destination?.route ?: Screen.Groups.name )
    val currentScreen = allScreens.find { it.route == backStackEntry?.destination?.route } ?: Screen.AllGroups
    val test = testScreen.contains(currentScreen)

    Scaffold(
        topBar = {
            TopBarSample(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                test = test,
                navigateUp = { navController.navigateUp() }
            )
        },
        floatingActionButton = {
            FabButtonSample(navController = navController, currentScreen = currentScreen)
        },
//        bottomBar = {
//            if (currentScreen == Screen.AllGroups || currentScreen == Screen.AllDraws) {
//                NavigationBarSample(navController)
//            }
//        },

        ) { innerPadding ->
        NavHost(navController = navController, startDestination = Root.Groups.route, Modifier.padding(innerPadding)) {
            navigation(startDestination = Screen.AllGroups.route, route = Root.Groups.route) {
                composable(Screen.AllGroups.route) { GroupScreen() }
                composable(Screen.NewGroup.route) {
                    NewGroupScreen(navigateBack = { navController.popBackStack() }) }
            }
            navigation(startDestination = Screen.AllDraws.route, route = Root.Draws.route) {
                composable(Screen.AllDraws.route) { QuestionScreen() }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarSample(
    currentScreen: Screen,
    canNavigateBack: Boolean,
    test: Boolean,
    navigateUp: () -> Unit,
    //modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                stringResource(currentScreen.title)
            )
        },
        navigationIcon = {
            if (canNavigateBack && test) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
//        actions = {
//            IconButton(onClick = { /* doSomething() */ }) {
//                Icon(
//                    imageVector = Icons.Filled.Favorite,
//                    contentDescription = "Localized description"
//                )
//            }
//        }
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FabButtonSample(navController: NavHostController, currentScreen: Screen) {
    val isVisible = currentScreen == Screen.AllGroups
    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn(),
        exit = scaleOut()
    ) {
        FloatingActionButton(
            onClick = { navController.navigate(Screen.NewGroup.route) },
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = stringResource(R.string.new_group)
            )
        }
    }
    if (currentScreen == Screen.AllGroups) {

    }
}

@Composable
fun NavigationBarSample(navController: NavHostController) {
    val items = listOf(Root.Groups, Root.Draws)

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = stringResource(screen.title)) },
                label = { Text(stringResource(screen.title)) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items

                        popUpTo(0) {// {navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // re selecting the same item
                        launchSingleTop = true
                        // Restore state when re selecting a previously selected item
                        //restoreState = true
                    }

                }

            )
        }
    }
}
