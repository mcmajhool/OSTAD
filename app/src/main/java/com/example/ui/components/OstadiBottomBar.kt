package com.example.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.outlined.Class
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Leaderboard
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.AppStrings
import com.example.ui.navigation.OstadiScreen
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.OstadiDarkBg

data class BottomNavItem(
    val screen: OstadiScreen,
    val title: (AppStrings.Strings) -> String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

val bottomNavItems = listOf(
    BottomNavItem(
        screen = OstadiScreen.DASHBOARD,
        title = { it.navDashboard },
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        testTag = "nav_dashboard_button"
    ),
    BottomNavItem(
        screen = OstadiScreen.CLASSROOMS,
        title = { it.navClassrooms },
        selectedIcon = Icons.Filled.Class,
        unselectedIcon = Icons.Outlined.Class,
        testTag = "nav_classrooms_button"
    ),
    BottomNavItem(
        screen = OstadiScreen.GRADES,
        title = { it.navGrades },
        selectedIcon = Icons.Filled.Assessment,
        unselectedIcon = Icons.Outlined.Assessment,
        testTag = "nav_grades_button"
    ),
    BottomNavItem(
        screen = OstadiScreen.STATISTICS,
        title = { it.navStatistics },
        selectedIcon = Icons.Filled.Leaderboard,
        unselectedIcon = Icons.Outlined.Leaderboard,
        testTag = "nav_statistics_button"
    ),
    BottomNavItem(
        screen = OstadiScreen.PROFILE_SETTINGS,
        title = { it.navProfile },
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        testTag = "nav_profile_button"
    )
)

@Composable
fun OstadiBottomBar(
    currentScreen: OstadiScreen,
    language: AppLanguage,
    onNavigate: (OstadiScreen) -> Unit
) {
    val s = AppStrings.get(language)

    NavigationBar(
        containerColor = Color(0xFF09111E),
        modifier = Modifier.border(width = 1.dp, color = Color(0xFF1E2D4A).copy(alpha = 0.5f))
    ) {
        bottomNavItems.forEach { item ->
            val isSelected = currentScreen == item.screen
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.screen) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title(s),
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = item.title(s),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 11.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = GoldAccent,
                    selectedTextColor = GoldAccent,
                    indicatorColor = Color(0xFF2C2414),
                    unselectedIconColor = Color(0xFF64748B),
                    unselectedTextColor = Color(0xFF64748B)
                ),
                modifier = Modifier.testTag(item.testTag)
            )
        }
    }
}
