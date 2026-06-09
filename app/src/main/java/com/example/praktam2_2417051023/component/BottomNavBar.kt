package com.example.praktam2_2417051023.component

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.praktam2_2417051023.ui.theme.GreenPrimary
import com.example.praktam2_2417051023.ui.theme.GreenSoft

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

val bottomNavItems = listOf(
    BottomNavItem("Beranda", Icons.Filled.Home, "home"),
    BottomNavItem("Quiz", Icons.Filled.Quiz, "history"),
    BottomNavItem("Kategori", Icons.Filled.GridView, "kategori"),
    BottomNavItem("Profile", Icons.Filled.Person, "profile")
)

@Composable
fun BottomNavBar(
    navController: NavController
) {
    val currentRoute = navController
        .currentBackStackEntryAsState()
        .value
        ?.destination
        ?.route

    NavigationBar(
        containerColor = Color.White,
        modifier = Modifier.height(64.dp)
    ) {
        bottomNavItems.forEach { item ->

            val isSelected = when (item.route) {
                "home" -> currentRoute == "home"

                "history" -> currentRoute == "history" ||
                        currentRoute == "quiz/{namaHewan}" ||
                        currentRoute == "result/{namaHewan}/{score}/{total}"

                "kategori" -> currentRoute == "kategori"

                "profile" -> currentRoute == "profile"

                else -> false
            }

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navController.navigate(item.route) {
                            popUpTo("home") {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) {
                            FontWeight.Bold
                        } else {
                            FontWeight.Normal
                        }
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = GreenPrimary,
                    selectedTextColor = GreenPrimary,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = GreenSoft
                )
            )
        }
    }
}