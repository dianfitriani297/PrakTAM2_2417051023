package com.example.praktam2_2417051023

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.praktam2_2417051023.model.MitosFaktaHewan
import com.example.praktam2_2417051023.model.MitosFaktaHewanSource
import com.example.praktam2_2417051023.ui.theme.ZoopediaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZoopediaTheme {
                ZoopediaScreen()
            }
        }
    }
}

fun warnaButton(warna: Color): Color {
    return when (warna) {
        Color(0xFFE3F2FD) -> Color(0xFF2196F3)
        Color(0xFFFFF3E0) -> Color(0xFFFF9800)
        Color(0xFFE8F5E9) -> Color(0xFF4CAF50)
        Color(0xFFFCE4EC) -> Color(0xFFE91E63)
        Color(0xFFF3E5F5) -> Color(0xFF9C27B0)
        else -> Color.DarkGray
    }
}

@Composable
fun ZoopediaScreen() {
    val listWarna = listOf(
        Color(0xFFE3F2FD), Color(0xFFFFF3E0), Color(0xFFE8F5E9),
        Color(0xFFFCE4EC), Color(0xFFF3E5F5)
    )

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val loadingMap = remember { mutableStateMapOf<String, Boolean>() }
    val finishedList = remember { mutableStateMapOf<String, Boolean>() }

    var currentScreen by remember { mutableStateOf("home") }
    var selectedHewan by remember { mutableStateOf<MitosFaktaHewan?>(null) }
    val favList = remember { mutableStateMapOf<String, Boolean>() }
    var searchText by remember { mutableStateOf("") }

    val filteredList = MitosFaktaHewanSource.daftarMitosFaktaHewan.filter {
        it.namaHewan.contains(searchText, ignoreCase = true)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (currentScreen == "home") {
            LazyColumn(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(text = "Zoopedia", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                }

                item {
                    TextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        placeholder = { Text("Cari hewan...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                }

                if (filteredList.isEmpty()) {
                    item { Text(text = "Hewan tidak ditemukan", color = Color.Gray) }
                } else {
                    item { Text(text = "Rekomendasi", style = MaterialTheme.typography.titleMedium) }

                    item {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(filteredList) { hewan ->
                                Card(modifier = Modifier.width(140.dp), shape = RoundedCornerShape(16.dp)) {
                                    Column {
                                        Image(painter = painterResource(id = hewan.imageRes), contentDescription = null, modifier = Modifier.fillMaxWidth().height(100.dp), contentScale = ContentScale.Crop)
                                        Text(text = hewan.namaHewan, modifier = Modifier.padding(8.dp))
                                    }
                                }
                            }
                        }
                    }

                    item { Text(text = "Daftar Hewan", style = MaterialTheme.typography.titleMedium) }

                    items(filteredList) { hewan ->
                        val index = MitosFaktaHewanSource.daftarMitosFaktaHewan.indexOf(hewan)
                        val bgCard = listWarna[index % listWarna.size]
                        val warnaBtn = warnaButton(bgCard)
                        val isLoading = loadingMap[hewan.namaHewan] ?: false
                        val isFinished = finishedList[hewan.namaHewan] ?: false

                        Card(
                            shape = RoundedCornerShape(28.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isFinished) Color(0xFF333333) else bgCard
                            ),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Box {
                                    Image(
                                        painter = painterResource(id = hewan.imageRes),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(170.dp)
                                            .clip(RoundedCornerShape(20.dp)),
                                        contentScale = ContentScale.Crop,
                                        colorFilter = if (isFinished) ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) }) else null,
                                        alpha = if (isFinished) 0.4f else 1f
                                    )
                                    IconButton(
                                        onClick = { if (!isFinished) favList[hewan.namaHewan] = !(favList[hewan.namaHewan] ?: false) },
                                        modifier = Modifier.align(Alignment.TopEnd)
                                    ) {
                                        Icon(
                                            imageVector = if (favList[hewan.namaHewan] == true) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                            contentDescription = null,
                                            tint = if (favList[hewan.namaHewan] == true) Color.Red else Color.White
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = hewan.namaHewan,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = if (isFinished) Color.Gray else Color.Unspecified
                                )
                                Text(
                                    text = "Mitos atau fakta tentang ${hewan.namaHewan.lowercase()}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (isFinished) Color.DarkGray else Color.Unspecified
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = {
                                        scope.launch {
                                            if (isFinished) {
                                                snackbarHostState.showSnackbar("Kuis ${hewan.namaHewan} sudah kamu selesaikan!")
                                            } else {
                                                loadingMap[hewan.namaHewan] = true
                                                delay(2000)
                                                loadingMap[hewan.namaHewan] = false
                                                selectedHewan = hewan
                                                currentScreen = "quiz"
                                            }
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isFinished) Color.Black else warnaBtn
                                    ),
                                    enabled = !isLoading
                                ) {
                                    if (isLoading) {
                                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Menyiapkan...")
                                    } else {
                                        Text(if (isFinished) "Selesai" else "Mulai")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            selectedHewan?.let { hewan ->
                QuizPage(
                    hewan = hewan,
                    onBack = { currentScreen = "home" },
                    onAnswer = {
                        scope.launch {
                            finishedList[hewan.namaHewan] = true
                            snackbarHostState.showSnackbar("Quiz ${hewan.namaHewan} selesai!")
                        }
                    }
                )
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 32.dp)
        )
    }
}

@Composable
fun QuizPage(hewan: MitosFaktaHewan, onBack: () -> Unit, onAnswer: () -> Unit) {
    var clickedButton by remember { mutableStateOf<String?>(null) }
    val soal = when(hewan.namaHewan) {
        "Gajah" -> "gajah memiliki ingatan yang sangat kuat?"
        "Singa" -> "singa jantan adalah pemburu utama dalam kelompok?"
        "Panda" -> "panda termasuk ke dalam keluarga beruang?"
        "Zebra" -> "warna kulit asli zebra adalah putih?"
        "Harimau" -> "harimau menyukai air dan pandai berenang?"
        else -> "Apakah ini fakta atau mitos?"
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Quiz ${hewan.namaHewan}", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(24.dp))
        Image(painter = painterResource(id = hewan.imageRes), contentDescription = null, modifier = Modifier.fillMaxWidth().height(250.dp).clip(RoundedCornerShape(20.dp)), contentScale = ContentScale.Crop)
        Spacer(modifier = Modifier.height(32.dp))
        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = soal, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(32.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(
                        onClick = { if(clickedButton == null) { clickedButton = "mitos"; onAnswer() } },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = if (clickedButton == "mitos") Color.Gray else Color(0xFFFF9800)),
                        enabled = clickedButton == null
                    ) { Text("Mitos") }
                    Button(
                        onClick = { if(clickedButton == null) { clickedButton = "fakta"; onAnswer() } },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = if (clickedButton == "fakta") Color.Gray else Color(0xFF4CAF50)),
                        enabled = clickedButton == null
                    ) { Text("Fakta") }
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        TextButton(onClick = onBack) { Text("Kembali ke Beranda") }
    }
}