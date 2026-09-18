package com.ashish.stash.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ashish.stash.ui.component.StashLogo
import com.ashish.stash.ui.feature.document.DocumentDetailScreen
import com.ashish.stash.ui.feature.home.HomeScreen
import com.ashish.stash.ui.feature.onboarding.OnboardingScreen
import com.ashish.stash.ui.feature.onboarding.OnboardingViewModel
import com.ashish.stash.ui.feature.priority.PriorityModeScreen
import com.ashish.stash.ui.feature.search.SearchScreen
import com.ashish.stash.ui.feature.settings.*
import com.ashish.stash.ui.feature.splash.SplashNavigation
import com.ashish.stash.ui.feature.splash.SplashViewModel
import com.ashish.stash.ui.feature.viewer.ViewerScreen
import com.ashish.stash.ui.theme.InkNavy

@Composable
fun StashNavHost(
    navController: NavHostController,
    onOpenDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Destination.Splash,
        modifier = modifier
    ) {
        composable<Destination.Splash> {
            val viewModel: SplashViewModel = hiltViewModel()
            LaunchedEffect(Unit) {
                viewModel.navigationEvent.collect { event ->
                    when (event) {
                        SplashNavigation.ToHome -> {
                            navController.navigate(Destination.Home) {
                                popUpTo<Destination.Splash> { inclusive = true }
                            }
                        }
                        SplashNavigation.ToOnboarding -> {
                            navController.navigate(Destination.Onboarding) {
                                popUpTo<Destination.Splash> { inclusive = true }
                            }
                        }
                    }
                }
            }
            
            Box(
                modifier = Modifier.fillMaxSize().background(InkNavy),
                contentAlignment = Alignment.Center
            ) {
                StashLogo(modifier = Modifier.size(240.dp))
            }
        }

        composable<Destination.Onboarding> {
            val viewModel: OnboardingViewModel = hiltViewModel()
            LaunchedEffect(Unit) {
                viewModel.navigationEvent.collect {
                    navController.navigate(Destination.Home) {
                        popUpTo<Destination.Onboarding> { inclusive = true }
                    }
                }
            }
            OnboardingScreen(
                onComplete = { viewModel.completeOnboarding() }
            )
        }

        composable<Destination.Home> {
            HomeScreen(
                onOpenDrawer = onOpenDrawer,
                onNavigateToViewer = { id -> navController.navigate(Destination.Viewer(id)) },
                onNavigateToSearch = { navController.navigate(Destination.Search) },
                onNavigateToSettings = { navController.navigate(Destination.Settings) },
                onNavigateToPriority = { navController.navigate(Destination.Priority) }
            )
        }
        
        composable<Destination.DocumentDetail> {
            DocumentDetailScreen(
                onNavigateBack = { navController.popBackStack() },
                onViewDocument = { id -> navController.navigate(Destination.Viewer(id)) }
            )
        }
        
        composable<Destination.Search> {
            SearchScreen(
                onOpenDrawer = onOpenDrawer,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToViewer = { id -> navController.navigate(Destination.Viewer(id)) }
            )
        }

        composable<Destination.Priority> {
            PriorityModeScreen(
                onOpenDrawer = onOpenDrawer,
                onNavigateToViewer = { id -> navController.navigate(Destination.Viewer(id)) }
            )
        }

        composable<Destination.Settings> {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAbout = { navController.navigate(Destination.About) },
                onNavigateToPrivacy = { navController.navigate(Destination.Privacy) },
                onNavigateToHelp = { navController.navigate(Destination.Help) },
                onNavigateToLicenses = { navController.navigate(Destination.Licenses) }
            )
        }

        composable<Destination.About> {
            AboutScreen(
                onNavigateBack = { navController.popBackStack() },
                onLicensesClick = { navController.navigate(Destination.Licenses) },
                onPrivacyClick = { navController.navigate(Destination.Privacy) },
                onHelpFaqClick = { navController.navigate(Destination.Help) }
            )
        }

        composable<Destination.Privacy> {
            PrivacyScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable<Destination.Help> {
            HelpFaqScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable<Destination.Licenses> {
            LicensesScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable<Destination.Viewer> {
            ViewerScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetails = { id -> navController.navigate(Destination.DocumentDetail(id)) }
            )
        }
    }
}
