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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
        Color(0xFFE3F2FD),
        Color(0xFFFFF3E0),
        Color(0xFFE8F5E9),
        Color(0xFFFCE4EC),
        Color(0xFFF3E5F5)
    )

    val favList = remember { mutableStateMapOf<String, Boolean>() }

    var searchText by remember { mutableStateOf("") }

    val filteredList = MitosFaktaHewanSource.daftarMitosFaktaHewan.filter {
        it.namaHewan.contains(searchText, ignoreCase = true)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            Text(
                text = "Zoopedia",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("Cari hewan...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }

        if (filteredList.isEmpty()) {

            item {
                Text(
                    text = "Hewan tidak ditemukan",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

        } else {

            item {
                Text(
                    text = "Rekomendasi Hewan",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredList) { hewan ->
                        Card(
                            modifier = Modifier.width(140.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Column {
                                Image(
                                    painter = painterResource(id = hewan.imageRes),
                                    contentDescription = hewan.namaHewan,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp),
                                    contentScale = ContentScale.Crop
                                )

                                Text(
                                    text = hewan.namaHewan,
                                    modifier = Modifier.padding(8.dp),
                                    style = MaterialTheme.typography.titleSmall
                                )
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Daftar Hewan",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            items(filteredList) { hewan ->

                val index = MitosFaktaHewanSource.daftarMitosFaktaHewan.indexOf(hewan)
                val bgCard = listWarna[index % listWarna.size]
                val warnaBtn = warnaButton(bgCard)

                val fav = favList[hewan.namaHewan] ?: false

                Card(
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = bgCard
                    ),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {

                    Column(modifier = Modifier.padding(16.dp)) {

                        Box {
                            Image(
                                painter = painterResource(id = hewan.imageRes),
                                contentDescription = hewan.namaHewan,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(170.dp)
                                    .clip(RoundedCornerShape(20.dp)),
                                contentScale = ContentScale.Crop
                            )

                            IconButton(
                                onClick = {
                                    favList[hewan.namaHewan] = !fav
                                },
                                modifier = Modifier.align(Alignment.TopEnd)
                            ) {
                                Icon(
                                    imageVector = if (fav)
                                        Icons.Filled.Favorite
                                    else
                                        Icons.Outlined.FavoriteBorder,
                                    contentDescription = "Favorite",
                                    tint = if (fav) Color.Red else Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = hewan.namaHewan,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = "Kumpulan mitos dan fakta tentang ${hewan.namaHewan.lowercase()}",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = warnaBtn
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "Mulai",
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}