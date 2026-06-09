package com.example.praktam2_2417051023.screen

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.praktam2_2417051023.component.BottomNavBar
import com.example.praktam2_2417051023.component.ZoopediaImage
import com.example.praktam2_2417051023.data.datastore.UserPreferencesRepository
import com.example.praktam2_2417051023.data.model.MitosFaktaHewan
import com.example.praktam2_2417051023.ui.theme.GreenPrimary
import com.example.praktam2_2417051023.ui.theme.OrangeButton

@Composable
fun HistoryPage(
    navController: NavController,
    hewanList: List<MitosFaktaHewan>
) {
    val context = LocalContext.current
    val userRepo = remember { UserPreferencesRepository(context) }

    var kuisSelesaiList by remember {
        mutableStateOf<List<Pair<MitosFaktaHewan, Int>>>(emptyList())
    }

    var kuisBelumList by remember {
        mutableStateOf<List<MitosFaktaHewan>>(emptyList())
    }

    var isLoading by remember { mutableStateOf(true) }
    var hasInternet by remember { mutableStateOf(true) }

    LaunchedEffect(hewanList) {
        hasInternet = isHistoryInternetAvailable(context)

        if (!hasInternet || hewanList.isEmpty()) {
            kuisSelesaiList = emptyList()
            kuisBelumList = emptyList()
            isLoading = false
            return@LaunchedEffect
        }

        val selesaiTemp = mutableListOf<Pair<MitosFaktaHewan, Int>>()
        val belumTemp = mutableListOf<MitosFaktaHewan>()

        hewanList.forEach { hewan ->
            val nama = hewan.namaHewan

            if (userRepo.isSelesai(nama)) {
                val skor = userRepo.getSkor(nama)
                selesaiTemp.add(Pair(hewan, skor))
            } else {
                belumTemp.add(hewan)
            }
        }

        kuisSelesaiList = selesaiTemp
        kuisBelumList = belumTemp
        isLoading = false
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        bottomBar = {
            BottomNavBar(navController)
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali",
                        tint = GreenPrimary
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "Daftar & Riwayat Quiz",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = GreenPrimary
                )
            }

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                color = GreenPrimary,
                                strokeWidth = 4.dp
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Memuat Riwayat Quiz...",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }
                }

                !hasInternet || hewanList.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Gagal Memuat Data",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.Red,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Pastikan koneksi internet kamu menyala",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = 0.dp,
                            bottom = 44.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            Text(
                                text = "Sudah Diselesaikan",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.Black
                            )
                        }

                        if (kuisSelesaiList.isEmpty()) {
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = "Belum ada kuis yang kamu selesaikan.",
                                        color = Color.Gray,
                                        fontSize = 13.sp,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }
                        } else {
                            items(kuisSelesaiList) { itemKuis ->
                                val hewan = itemKuis.first
                                val skor = itemKuis.second

                                ItemKuisRow(
                                    hewan = hewan,
                                    skorTinggi = skor,
                                    navController = navController,
                                    isSelesai = true
                                )
                            }
                        }

                        item {
                            Text(
                                text = "Belum Dimainkan",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.Black,
                                modifier = Modifier.padding(top = 14.dp)
                            )
                        }

                        if (kuisBelumList.isEmpty()) {
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = "Semua kuis hewan sudah kamu selesaikan! Luar biasa! 🎉",
                                        color = GreenPrimary,
                                        fontSize = 13.sp,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }
                        } else {
                            items(kuisBelumList) { hewan ->
                                ItemKuisRow(
                                    hewan = hewan,
                                    skorTinggi = null,
                                    navController = navController,
                                    isSelesai = false
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemKuisRow(
    hewan: MitosFaktaHewan,
    skorTinggi: Int?,
    navController: NavController,
    isSelesai: Boolean
) {
    val totalSoal = hewan.soal.size

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ZoopediaImage(
                imageUrl = hewan.imageUrl,
                contentDescription = hewan.namaHewan,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(14.dp)),
                contentScale = ContentScale.Crop,
                alignment = Alignment.TopCenter
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = hewan.namaHewan.uppercase(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = if (skorTinggi != null) {
                        "Skor Terbaik: $skorTinggi / $totalSoal"
                    } else {
                        "$totalSoal Pertanyaan"
                    },
                    fontSize = 12.sp,
                    color = if (skorTinggi != null) {
                        GreenPrimary
                    } else {
                        Color.Gray
                    },
                    fontWeight = if (skorTinggi != null) {
                        FontWeight.SemiBold
                    } else {
                        FontWeight.Normal
                    }
                )
            }

            Button(
                onClick = {
                    navController.navigate("quiz/${hewan.namaHewan}")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelesai) {
                        OrangeButton
                    } else {
                        GreenPrimary
                    }
                ),
                shape = RoundedCornerShape(50.dp),
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 0.dp
                ),
                modifier = Modifier.height(36.dp)
            ) {
                Text(
                    text = if (isSelesai) "Ulangi" else "Mulai",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

fun isHistoryInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager

    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}