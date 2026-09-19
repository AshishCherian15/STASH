package com.ashish.stash.ui.navigation

import android.graphics.Color
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import com.ashish.stash.core.security.SecuritySessionManager
import com.ashish.stash.ui.component.StashLogo
import com.ashish.stash.ui.feature.home.HomeViewModel
import com.ashish.stash.ui.feature.settings.SettingsViewModel
import com.ashish.stash.ui.theme.StashBlue

@Composable
fun StashDrawer(
    currentDestination: NavDestination?,
    onNavigate: (Destination) -> Unit,
    securitySessionManager: SecuritySessionManager,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val settingsState by settingsViewModel.uiState.collectAsState()
    val itemsUnlocked by securitySessionManager.itemsUnlocked.collectAsState()

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
            Spacer(Modifier.width(16.dp))
            Text("Stash", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        }
        
        Button(
            onClick = { 
                if (itemsUnlocked) securitySessionManager.lockItems() 
                else securitySessionManager.unlockItems() 
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 28.dp, vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (itemsUnlocked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
            )
        ) {
            Icon(if (itemsUnlocked) Icons.Outlined.LockOpen else Icons.Outlined.Lock, null)
            Spacer(Modifier.width(8.dp))
            Text(if (itemsUnlocked) "Lock Vault Items" else "Unlock Vault Items")
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
        
        LazyColumn(modifier = Modifier.weight(1f)) {
            item {
                DrawerItem(
                    label = "All Documents",
                    icon = Icons.Outlined.Home,
                    selected = currentDestination?.hasRoute(Destination.Home::class) == true,
                    onClick = { 
                        homeViewModel.setCategoryFilter(null)
                        onNavigate(Destination.Home) 
                    }
                )
            }
            item {
                DrawerItem(
                    label = "Search",
                    icon = Icons.Outlined.Search,
                    selected = currentDestination?.hasRoute(Destination.Search::class) == true,
                    onClick = { onNavigate(Destination.Search) }
                )
            }
            item {
                DrawerItem(
                    label = "Priority Mode",
                    icon = Icons.Outlined.Star,
                    selected = currentDestination?.hasRoute(Destination.Priority::class) == true,
                    onClick = { onNavigate(Destination.Priority) }
                )
            }

            if (settingsState.categories.isNotEmpty()) {
                item { DrawerSectionHeader("Categories") }
                items(settingsState.categories) { category ->
                    DrawerSubItem(
                        label = category.name,
                        icon = Icons.Outlined.Category,
                        color = category.color,
                        onClick = { 
                            homeViewModel.setCategoryFilter(category.categoryId)
                            onNavigate(Destination.Home)
                        }
                    )
                }
            }

            if (settingsState.folders.isNotEmpty()) {
                item { DrawerSectionHeader("Folders") }
                items(settingsState.folders) { folder ->
                    DrawerSubItem(
                        label = folder.name,
                        icon = Icons.Outlined.Folder,
                        color = folder.color,
                        onClick = { 
                            homeViewModel.setFolderFilter(folder.folderId)
                            onNavigate(Destination.Home)
                        }
                    )
                }
            }
        }
        
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
        
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
private fun DrawerSectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 28.dp, vertical = 8.dp)
    )
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
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            selectedIconColor = MaterialTheme.colorScheme.primary,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}

@Composable
private fun DrawerSubItem(
    label: String,
    icon: ImageVector,
    color: String? = null,
    onClick: () -> Unit
) {
    val tint = color?.let { runCatching { androidx.compose.ui.graphics.Color(Color.parseColor(it)) }.getOrNull() }
        ?: MaterialTheme.colorScheme.onSurfaceVariant

    ListItem(
        headlineContent = { Text(label, style = MaterialTheme.typography.bodyMedium) },
        leadingContent = { Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp), tint = tint) },
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(start = 12.dp)
            .fillMaxWidth(),
        colors = ListItemDefaults.colors(containerColor = androidx.compose.ui.graphics.Color.Transparent)
    )
}
