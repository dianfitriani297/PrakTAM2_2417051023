package com.example.praktam2_2417051023.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
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
import com.example.praktam2_2417051023.data.model.MitosFaktaHewan
import com.example.praktam2_2417051023.data.repository.HewanRepository
import com.example.praktam2_2417051023.data.datastore.UserPreferencesRepository
import com.example.praktam2_2417051023.ui.theme.GreenPrimary
import com.example.praktam2_2417051023.ui.theme.GreenSoft
import kotlinx.coroutines.launch

@Composable
fun HomePage(
    navController: NavController,
    onHewanLoaded: (List<MitosFaktaHewan>) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val hewanRepo = remember { HewanRepository() }
    val userRepo = remember { UserPreferencesRepository(context) }

    var hewanList by remember { mutableStateOf<List<MitosFaktaHewan>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    var namaPanggilan by remember { mutableStateOf("") }

    var favMap by remember { mutableStateOf<Map<String, Boolean>>(emptyMap()) }
    var selesaiMap by remember { mutableStateOf<Map<String, Boolean>>(emptyMap()) }
    var skorMap by remember { mutableStateOf<Map<String, Int>>(emptyMap()) }

    var isSearchFocused by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            namaPanggilan = userRepo.getNamaPanggilan()

            val response = hewanRepo.getHewan()

            if (response.isEmpty()) {
                isLoading = false
                isError = true
                return@LaunchedEffect
            }

            val favTemp = mutableMapOf<String, Boolean>()
            val selesaiTemp = mutableMapOf<String, Boolean>()
            val skorTemp = mutableMapOf<String, Int>()

            response.forEach { hewan ->
                val nama = hewan.namaHewan
                favTemp[nama] = userRepo.getFavorit(nama)
                selesaiTemp[nama] = userRepo.isSelesai(nama)
                skorTemp[nama] = userRepo.getSkor(nama)
            }

            hewanList = response
            onHewanLoaded(response)

            favMap = favTemp
            selesaiMap = selesaiTemp
            skorMap = skorTemp

            isLoading = false
            isError = false
        } catch (_: Exception) {
            isLoading = false
            isError = true
        }
    }

    val displayList = hewanList.filter { hewan ->
        hewan.namaHewan.contains(searchText, ignoreCase = true)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        bottomBar = {
            BottomNavBar(navController)
        }
    ) { paddingValues ->

        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
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
                            text = "Memuat Dunia Hewan...",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }

            isError -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Gagal Memuat Data",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Red,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Pastikan koneksi internet kamu menyala",
                            textAlign = TextAlign.Center,
                            color = Color.Gray
                        )
                    }
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(bottom = paddingValues.calculateBottomPadding()),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 0.dp,
                        bottom = 44.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp, bottom = 2.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Zoopedia",
                                fontSize = 28.sp,
                                color = GreenPrimary,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            )

                            IconButton(
                                onClick = {
                                    navController.navigate("profile")
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.AccountCircle,
                                    contentDescription = "Ke Halaman Profil",
                                    tint = GreenPrimary,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }
                    }

                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = GreenSoft
                            ),
                            elevation = CardDefaults.cardElevation(1.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Halo, ${namaPanggilan.uppercase()}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )

                                Text(
                                    text = "Mau belajar hewan apa hari ini?",
                                    fontSize = 13.sp,
                                    color = Color.DarkGray
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(40.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color.White,
                                    border = BorderStroke(
                                        width = if (isSearchFocused) 1.5.dp else 0.dp,
                                        color = if (isSearchFocused) GreenPrimary else Color.Transparent
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Search,
                                            contentDescription = "Cari Hewan",
                                            tint = if (isSearchFocused) GreenPrimary else Color.Gray,
                                            modifier = Modifier.size(18.dp)
                                        )

                                        Spacer(modifier = Modifier.width(8.dp))

                                        Box(
                                            modifier = Modifier.weight(1f),
                                            contentAlignment = Alignment.CenterStart
                                        ) {
                                            if (searchText.isEmpty()) {
                                                Text(
                                                    text = "cari nama hewan yang ingin dijelajah",
                                                    color = Color.Gray,
                                                    fontSize = 12.sp
                                                )
                                            }

                                            BasicTextField(
                                                value = searchText,
                                                onValueChange = {
                                                    searchText = it
                                                },
                                                textStyle = MaterialTheme.typography.bodyMedium.copy(
                                                    fontSize = 13.sp,
                                                    color = Color.Black
                                                ),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .onFocusChanged { focusState ->
                                                        isSearchFocused = focusState.isFocused
                                                    },
                                                singleLine = true
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Rekomendasi Hewan",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(displayList) { hewan ->
                                Card(
                                    modifier = Modifier
                                        .width(110.dp)
                                        .height(120.dp)
                                        .clickable {
                                            navController.navigate("quiz/${hewan.namaHewan}")
                                        },
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.White
                                    ),
                                    elevation = CardDefaults.cardElevation(2.dp)
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        ZoopediaImage(
                                            imageUrl = hewan.imageUrl,
                                            contentDescription = hewan.namaHewan,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(85.dp)
                                                .clip(
                                                    RoundedCornerShape(
                                                        topStart = 14.dp,
                                                        topEnd = 14.dp
                                                    )
                                                ),
                                            contentScale = ContentScale.Crop
                                        )

                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .weight(1f),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = hewan.namaHewan,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Black,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Eksplorasi Hewan",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }

                    if (displayList.isEmpty()) {
                        item {
                            Text(
                                text = "Hewan tidak ditemukan",
                                color = Color.Gray,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    } else {
                        items(displayList) { hewan ->
                            val nama = hewan.namaHewan
                            val isSelesai = selesaiMap[nama] ?: false
                            val isFav = favMap[nama] ?: false
                            val skor = skorMap[nama] ?: 0
                            val total = hewan.soal.size

                            val warnaTombol = if (isSelesai) {
                                Color(0xFFFF9800)
                            } else {
                                GreenPrimary
                            }

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                                elevation = CardDefaults.cardElevation(3.dp)
                            ) {
                                Column {
                                    Box {
                                        ZoopediaImage(
                                            imageUrl = hewan.imageUrl,
                                            contentDescription = nama,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(210.dp)
                                                .clip(
                                                    RoundedCornerShape(
                                                        topStart = 20.dp,
                                                        topEnd = 20.dp
                                                    )
                                                ),
                                            contentScale = ContentScale.Crop,
                                            alignment = Alignment.TopCenter
                                        )

                                        Surface(
                                            modifier = Modifier
                                                .align(Alignment.BottomStart)
                                                .padding(10.dp),
                                            shape = RoundedCornerShape(50.dp),
                                            color = Color(0xFF333333)
                                        ) {
                                            Text(
                                                text = if (isSelesai) "$skor/$total" else "$total Soal",
                                                modifier = Modifier.padding(
                                                    horizontal = 10.dp,
                                                    vertical = 4.dp
                                                ),
                                                color = Color.White,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }

                                        IconButton(
                                            onClick = {
                                                scope.launch {
                                                    val favoritBaru = !isFav

                                                    userRepo.setFavorit(nama, favoritBaru)

                                                    favMap = favMap.toMutableMap().also {
                                                        it[nama] = favoritBaru
                                                    }
                                                }
                                            },
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .padding(12.dp)
                                                .size(28.dp)
                                        ) {
                                            Icon(
                                                imageVector = if (isFav) {
                                                    Icons.Filled.Favorite
                                                } else {
                                                    Icons.Outlined.FavoriteBorder
                                                },
                                                contentDescription = "Favorit Hewan",
                                                tint = if (isFav) Color.Red else Color.White,
                                                modifier = Modifier.size(26.dp)
                                            )
                                        }
                                    }

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 14.dp, vertical = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(
                                                text = nama.uppercase(),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 16.sp,
                                                color = Color.Black
                                            )

                                            Text(
                                                text = "Kumpulan mitos & fakta tentang ${nama.lowercase()}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color.Gray
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(8.dp))

                                        OutlinedButton(
                                            onClick = {
                                                navController.navigate("quiz/$nama")
                                            },
                                            shape = RoundedCornerShape(50.dp),
                                            colors = ButtonDefaults.outlinedButtonColors(
                                                contentColor = warnaTombol
                                            ),
                                            border = BorderStroke(1.5.dp, warnaTombol)
                                        ) {
                                            Text(
                                                text = if (isSelesai) "Ulangi Quiz" else "Mulai Quiz",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}