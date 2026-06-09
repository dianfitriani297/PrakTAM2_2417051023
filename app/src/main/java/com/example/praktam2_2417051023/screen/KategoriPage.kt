package com.example.praktam2_2417051023.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.praktam2_2417051023.component.BottomNavBar
import com.example.praktam2_2417051023.component.ZoopediaImage
import com.example.praktam2_2417051023.data.model.KategoriHewan
import com.example.praktam2_2417051023.data.repository.KategoriRepository
import com.example.praktam2_2417051023.ui.theme.GreenPrimary
import com.example.praktam2_2417051023.ui.theme.KategoriColors

@Composable
fun KategoriPage(
    navController: NavController
) {
    val kategoriRepo = remember { KategoriRepository() }

    var kategoriList by remember { mutableStateOf<List<KategoriHewan>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            val response = kategoriRepo.getKategori()

            if (response.isEmpty()) {
                isLoading = false
                isError = true
                return@LaunchedEffect
            }

            kategoriList = response
            isLoading = false
            isError = false
        } catch (_: Exception) {
            isLoading = false
            isError = true
        }
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
                    text = "Kategori Hewan",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = GreenPrimary
                )
            }

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background),
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
                                text = "Memuat Kategori Hewan...",
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
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = 0.dp,
                            bottom = 44.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        itemsIndexed(kategoriList) { index, kategori ->
                            val warna = KategoriColors[index % KategoriColors.size]

                            KategoriCard(
                                kategori = kategori,
                                warna = warna
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun KategoriCard(
    kategori: KategoriHewan,
    warna: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .height(IntrinsicSize.Max)
                    .background(
                        color = warna,
                        shape = RoundedCornerShape(
                            topStart = 20.dp,
                            bottomStart = 20.dp
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                ZoopediaImage(
                    imageUrl = kategori.imageUrl,
                    contentDescription = kategori.namaKategori,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(14.dp)),
                    contentScale = ContentScale.FillWidth
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = kategori.namaKategori,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = warna
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = kategori.deskripsi,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF555555)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "CONTOH:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = warna
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    kategori.contoh.forEach { contoh ->
                        Surface(
                            shape = RoundedCornerShape(50.dp),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            Text(
                                text = contoh,
                                modifier = Modifier.padding(
                                    horizontal = 12.dp,
                                    vertical = 5.dp
                                ),
                                fontSize = 12.sp,
                                color = Color(0xFF444444),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}