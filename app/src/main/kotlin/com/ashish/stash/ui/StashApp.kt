package com.ashish.stash.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavDestination.Companion.hasRoute
import com.ashish.stash.ui.navigation.*
import kotlinx.coroutines.launch

@Composable
fun StashApp() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Identify if current route should show sidebar
    val showSidebar = currentDestination?.let { dest ->
        dest.hasRoute(Destination.Home::class) || 
        dest.hasRoute(Destination.Search::class) || 
        dest.hasRoute(Destination.Priority::class)
    } ?: false

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = showSidebar,
        drawerContent = {
            StashDrawer(
                currentDestination = currentDestination,
                onNavigate = { destination ->
                    scope.launch { drawerState.close() }
                    navController.navigate(destination) {
                        popUpTo(Destination.Home) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            StashNavHost(
                navController = navController,
                onOpenDrawer = { scope.launch { drawerState.open() } }
            )
        }
    }
}
