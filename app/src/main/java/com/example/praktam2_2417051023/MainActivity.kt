package com.example.praktam2_2417051023

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.praktam2_2417051023.data.datastore.UserPreferencesRepository
import com.example.praktam2_2417051023.data.model.MitosFaktaHewan
import com.example.praktam2_2417051023.data.repository.AssetRepository
import com.example.praktam2_2417051023.screen.HistoryPage
import com.example.praktam2_2417051023.screen.HomePage
import com.example.praktam2_2417051023.screen.KategoriPage
import com.example.praktam2_2417051023.screen.LoginPage
import com.example.praktam2_2417051023.screen.ProfilePage
import com.example.praktam2_2417051023.screen.QuizPage
import com.example.praktam2_2417051023.screen.RegisterPage
import com.example.praktam2_2417051023.screen.ResultPage
import com.example.praktam2_2417051023.screen.SplashPage
import com.example.praktam2_2417051023.screen.WelcomePage
import com.example.praktam2_2417051023.ui.theme.ZoopediaTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ZoopediaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    val navController = rememberNavController()
                    val assetRepository = remember { AssetRepository() }
                    val userRepository = remember { UserPreferencesRepository(context) }

                    var logoUrlState by remember { mutableStateOf("") }
                    var globalHewanList by remember {
                        mutableStateOf<List<MitosFaktaHewan>>(emptyList())
                    }

                    NavHost(
                        navController = navController,
                        startDestination = "splash"
                    ) {
                        composable("splash") {
                            SplashPage()

                            LaunchedEffect(Unit) {
                                val assets = assetRepository.getAssets()

                                if (assets != null) {
                                    logoUrlState = assets.logoUrl
                                }

                                delay(1500)

                                val sudahLogin = userRepository.isLoggedIn()

                                if (sudahLogin) {
                                    navController.navigate("home") {
                                        popUpTo("splash") {
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                    }
                                } else {
                                    navController.navigate("welcome") {
                                        popUpTo("splash") {
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                    }
                                }
                            }
                        }

                        composable("welcome") {
                            WelcomePage(
                                navController = navController,
                                logoUrl = logoUrlState
                            )
                        }

                        composable("login") {
                            LoginPage(
                                navController = navController,
                                logoUrl = logoUrlState
                            )
                        }

                        composable("register") {
                            RegisterPage(
                                navController = navController,
                                logoUrl = logoUrlState
                            )
                        }

                        composable("home") {
                            HomePage(
                                navController = navController,
                                onHewanLoaded = { listHewan ->
                                    globalHewanList = listHewan
                                }
                            )
                        }

                        composable("kategori") {
                            KategoriPage(
                                navController = navController
                            )
                        }

                        composable("history") {
                            HistoryPage(
                                navController = navController,
                                hewanList = globalHewanList
                            )
                        }

                        composable("profile") {
                            ProfilePage(
                                navController = navController
                            )
                        }

                        composable(
                            route = "quiz/{namaHewan}",
                            arguments = listOf(
                                navArgument("namaHewan") {
                                    type = NavType.StringType
                                }
                            )
                        ) { backStackEntry ->
                            val namaHewan = backStackEntry.arguments
                                ?.getString("namaHewan")
                                .orEmpty()

                            val hewanDipilih = globalHewanList.find { hewan ->
                                hewan.namaHewan == namaHewan
                            }

                            if (hewanDipilih != null) {
                                QuizPage(
                                    hewan = hewanDipilih,
                                    navController = navController
                                )
                            } else {
                                navController.navigate("home") {
                                    popUpTo("home") {
                                        inclusive = false
                                    }
                                    launchSingleTop = true
                                }
                            }
                        }

                        composable(
                            route = "result/{namaHewan}/{score}/{total}",
                            arguments = listOf(
                                navArgument("namaHewan") {
                                    type = NavType.StringType
                                },
                                navArgument("score") {
                                    type = NavType.IntType
                                },
                                navArgument("total") {
                                    type = NavType.IntType
                                }
                            )
                        ) { backStackEntry ->
                            val namaHewan = backStackEntry.arguments
                                ?.getString("namaHewan")
                                .orEmpty()

                            val score = backStackEntry.arguments
                                ?.getInt("score") ?: 0

                            val total = backStackEntry.arguments
                                ?.getInt("total") ?: 5

                            val hewanDipilih = globalHewanList.find { hewan ->
                                hewan.namaHewan == namaHewan
                            }

                            if (hewanDipilih != null) {
                                ResultPage(
                                    hewan = hewanDipilih,
                                    score = score,
                                    total = total,
                                    navController = navController
                                )
                            } else {
                                navController.navigate("home") {
                                    popUpTo("home") {
                                        inclusive = false
                                    }
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}