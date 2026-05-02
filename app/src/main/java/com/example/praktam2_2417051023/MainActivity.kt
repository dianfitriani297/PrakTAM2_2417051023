package com.example.praktam2_2417051023

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.praktam2_2417051023.model.MitosFaktaHewan
import com.example.praktam2_2417051023.ui.theme.*
import com.example.praktam2_2417051023.network.RetrofitClient
import coil.compose.AsyncImage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ZoopediaTheme {
                val navController = rememberNavController()
                var hewanList by remember { mutableStateOf<List<MitosFaktaHewan>>(emptyList()) }

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        ZoopediaScreen(
                            navController = navController,
                            onHewanLoaded = { fetchedData -> hewanList = fetchedData }
                        )
                    }
                    composable("quiz/{namaHewan}") { backStackEntry ->
                        val nama = backStackEntry.arguments?.getString("namaHewan")
                        val hewan = hewanList.find { it.namaHewan == nama }
                        hewan?.let { QuizPage(hewan = it, navController = navController) }
                    }
                }
            }
        }
    }
}

fun warnaButton(warna: Color): Color {
    return when (warna) {
        BlueCard -> BlueButton
        OrangeCard -> OrangeButton
        GreenCard -> GreenButton
        PinkCard -> PinkButton
        PurpleCard -> PurpleButton
        else -> Color.DarkGray
    }
}

@Composable
fun ZoopediaScreen(navController: NavController, onHewanLoaded: (List<MitosFaktaHewan>) -> Unit) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var hewanListInternal by remember { mutableStateOf<List<MitosFaktaHewan>>(emptyList()) }
    var isLoadingData by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    val loadingMap = remember { mutableStateMapOf<MitosFaktaHewan, Boolean>() }
    val finishedList = remember { mutableStateMapOf<MitosFaktaHewan, Boolean>() }
    val favList = remember { mutableStateMapOf<MitosFaktaHewan, Boolean>() }
    var searchText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.instance.getHewan()
            hewanListInternal = response
            onHewanLoaded(response)
            isLoadingData = false
            isError = false
        } catch (e: Exception) {
            isLoadingData = false
            isError = true
            scope.launch { snackbarHostState.showSnackbar("Gagal memuat data: ${e.message}") }
        }
    }

    val displayList = hewanListInternal.filter { (it.namaHewan ?: "").contains(searchText, ignoreCase = true) }
    val listWarna = listOf(BlueCard, OrangeCard, GreenCard, PinkCard, PurpleCard)

    Box(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
        if (isLoadingData) {
            Box(modifier = Modifier.fillMaxSize().background(Color.White), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_zoopedia),
                        contentDescription = "Logo Zoopedia",
                        modifier = Modifier.size(280.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    CircularProgressIndicator(color = Color(0xFF4CAF50), strokeWidth = 4.dp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Loading...", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }
        } else if (isError) {
            Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Gagal Memuat Data", style = MaterialTheme.typography.titleLarge, color = Color.Red)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Pastikan koneksi internet Anda menyala", textAlign = TextAlign.Center, color = Color.Gray)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { Text("Zoopedia", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary) }
                item {
                    TextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        placeholder = { Text("Cari hewan...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFE0E0E0),
                            unfocusedContainerColor = Color(0xFFE0E0E0),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                }

                item {
                    Text("Rekomendasi", style = MaterialTheme.typography.titleMedium, color = Color.Black)
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(displayList) { hewan ->
                            val cardCol = listWarna[displayList.indexOf(hewan) % listWarna.size]
                            Card(
                                modifier = Modifier.width(130.dp).clickable { navController.navigate("quiz/${hewan.namaHewan ?: ""}") },
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = cardCol)
                            ) {
                                Column {
                                    AsyncImage(
                                        model = hewan.imageUrl,
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxWidth().height(100.dp).clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                                        contentScale = ContentScale.Crop,
                                        placeholder = painterResource(id = R.drawable.logo_zoopedia),
                                        error = painterResource(id = R.drawable.logo_zoopedia)
                                    )
                                    Text(hewan.namaHewan ?: "Hewan", modifier = Modifier.padding(12.dp), style = MaterialTheme.typography.bodyMedium, color = Color.Black)
                                }
                            }
                        }
                    }
                }

                if (displayList.isEmpty()) {
                    item { Text("Hewan tidak ditemukan", color = Color.Gray) }
                } else {
                    item { Text("Daftar Hewan", style = MaterialTheme.typography.titleMedium, color = Color.Black) }
                    items(displayList) { hewan ->
                        val bgCard = listWarna[displayList.indexOf(hewan) % listWarna.size]
                        val isFinished = finishedList[hewan] ?: false
                        Card(
                            shape = RoundedCornerShape(28.dp),
                            colors = CardDefaults.cardColors(containerColor = if (isFinished) Color(0xFF333333) else bgCard),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Box {
                                    AsyncImage(
                                        model = hewan.imageUrl,
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxWidth().height(170.dp).clip(RoundedCornerShape(20.dp)),
                                        contentScale = ContentScale.Crop,
                                        placeholder = painterResource(id = R.drawable.logo_zoopedia),
                                        error = painterResource(id = R.drawable.logo_zoopedia)
                                    )
                                    IconButton(
                                        onClick = { favList[hewan] = !(favList[hewan] ?: false) },
                                        modifier = Modifier.align(Alignment.TopEnd)
                                    ) {
                                        Icon(
                                            imageVector = if (favList[hewan] == true) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                            contentDescription = null,
                                            tint = if (favList[hewan] == true) Color.Red else Color.White
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(hewan.namaHewan ?: "Hewan", style = MaterialTheme.typography.titleMedium, color = if (isFinished) Color.White else Color.Black)
                                Text("Mitos atau fakta tentang ${(hewan.namaHewan ?: "").lowercase()}", style = MaterialTheme.typography.bodyMedium, color = if (isFinished) Color.LightGray else Color.DarkGray)
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = {
                                        scope.launch {
                                            loadingMap[hewan] = true
                                            delay(1500)
                                            loadingMap[hewan] = false
                                            navController.navigate("quiz/${hewan.namaHewan ?: ""}")
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    enabled = !(loadingMap[hewan] ?: false) && !isFinished,
                                    colors = ButtonDefaults.buttonColors(containerColor = if (isFinished) Color.Gray else warnaButton(bgCard), contentColor = Color.White)
                                ) {
                                    if (loadingMap[hewan] == true) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp, color = Color.White)
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("Menyiapkan...")
                                        }
                                    } else Text(if (isFinished) "Selesai" else "Mulai")
                                }
                            }
                        }
                    }
                }
            }
        }
        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 32.dp))
    }
}

@Composable
fun QuizPage(hewan: MitosFaktaHewan, navController: NavController) {
    var clickedButton by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val (soal, jawabanBenar, alasan) = when (hewan.namaHewan) {
        "Gajah" -> Triple("Apakah gajah memiliki ingatan yang kuat?", "Fakta", "Gajah punya otak terbesar di darat!")
        "Singa" -> Triple("Apakah singa jantan pemburu utama?", "Mitos", "Singa betina yang melakukan 90% perburuan.")
        "Panda" -> Triple("Apakah panda keluarga beruang?", "Fakta", "Secara genetik Panda adalah keluarga beruang.")
        "Zebra" -> Triple("Warna asli zebra adalah putih?", "Mitos", "Warna aslinya hitam dengan garis putih.")
        "Harimau" -> Triple("Harimau pandai berenang?", "Fakta", "Harimau adalah perenang yang sangat handal.")
        else -> Triple("Apakah ini fakta?", "Fakta", "Jawaban benar!")
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Quiz ${hewan.namaHewan ?: ""}", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(24.dp))
            AsyncImage(
                model = hewan.imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.logo_zoopedia),
                error = painterResource(id = R.drawable.logo_zoopedia)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE8F5E9), RoundedCornerShape(16.dp))
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(soal, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center, color = Color.Black)
                Spacer(modifier = Modifier.height(34.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = {
                            clickedButton = "mitos"
                            scope.launch { snackbarHostState.showSnackbar(if (jawabanBenar == "Mitos") "✅ Benar! $alasan" else "❌ Salah! $alasan") }
                        },
                        modifier = Modifier.weight(1f).height(34.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = OrangeMitos),
                        enabled = clickedButton == null
                    ) { Text("Mitos", color = Color.Black) }
                    Button(
                        onClick = {
                            clickedButton = "fakta"
                            scope.launch { snackbarHostState.showSnackbar(if (jawabanBenar == "Fakta") "✅ Benar! $alasan" else "❌ Salah! $alasan") }
                        },
                        modifier = Modifier.weight(1f).height(34.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        enabled = clickedButton == null
                    ) { Text("Fakta", color = Color.Black) }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth().navigationBarsPadding(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB0BEC5))
            ) { Text("Kembali ke Beranda", color = Color.Black) }
        }
        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 32.dp))
    }
}