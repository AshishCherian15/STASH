package com.ashish.stash.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import com.ashish.stash.ui.component.StashLogo
import com.ashish.stash.ui.theme.StashBlue

@Composable
fun StashDrawer(
    currentDestination: NavDestination?,
    onNavigate: (Destination) -> Unit,
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(
        modifier = modifier,
        drawerContainerColor = MaterialTheme.colorScheme.surface,
        drawerContentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        Spacer(Modifier.height(24.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 28.dp, vertical = 16.dp)
        ) {
            StashLogo(modifier = Modifier.size(60.dp))
            Spacer(Modifier.width(12.dp))
            Text(
                text = "Stash",
                style = MaterialTheme.typography.headlineMedium,
                color = StashBlue,
                fontWeight = FontWeight.Bold
            )
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = StashBlue.copy(alpha = 0.1f))
        
        DrawerItem(
            label = "Home",
            icon = Icons.Outlined.Home,
            selected = currentDestination?.hasRoute(Destination.Home::class) == true,
            onClick = { onNavigate(Destination.Home) }
        )
        DrawerItem(
            label = "Search",
            icon = Icons.Outlined.Search,
            selected = currentDestination?.hasRoute(Destination.Search::class) == true,
            onClick = { onNavigate(Destination.Search) }
        )
        DrawerItem(
            label = "Priority Mode",
            icon = Icons.Outlined.Star,
            selected = currentDestination?.hasRoute(Destination.Priority::class) == true,
            onClick = { onNavigate(Destination.Priority) }
        )
        
        Spacer(Modifier.weight(1f))
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = StashBlue.copy(alpha = 0.1f))
        
        DrawerItem(
            label = "Settings",
            icon = Icons.Outlined.Settings,
            selected = currentDestination?.hasRoute(Destination.Settings::class) == true,
            onClick = { onNavigate(Destination.Settings) }
        )
        DrawerItem(
            label = "About",
            icon = Icons.Outlined.Info,
            selected = currentDestination?.hasRoute(Destination.About::class) == true,
            onClick = { onNavigate(Destination.About) }
        )
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun DrawerItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = { Text(label) },
        icon = { Icon(icon, contentDescription = null) },
        selected = selected,
        onClick = onClick,
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = StashBlue.copy(alpha = 0.1f),
            selectedIconColor = StashBlue,
            selectedTextColor = StashBlue,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}
