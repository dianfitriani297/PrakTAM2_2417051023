package com.example.praktam2_2417051023

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.praktam2_2417051023.model.MitosFaktaHewanSource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZoopediaScreen()
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

    var searchText by remember { mutableStateOf("") }

    val filteredList = MitosFaktaHewanSource.daftarMitosFaktaHewan.filter {
        it.namaHewan.contains(searchText, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(top = 60.dp, bottom = 20.dp)
    ) {

        Text(
            text = "Zoopedia",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF333333),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = { Text("Cari hewan...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .background(Color(0xFFF5F5F5), RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )

        filteredList.forEachIndexed { index, hewan ->

            val bgCard = listWarna[index % listWarna.size]
            val warnaBtn = warnaButton(bgCard)

            var fav by remember { mutableStateOf(false) }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = bgCard),
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
                            onClick = { fav = !fav },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
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
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D2D2D)
                    )

                    Text(
                        text = "Kumpulan mitos dan fakta tentang ${hewan.namaHewan.lowercase()}",
                        fontSize = 14.sp,
                        color = Color.DarkGray.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = warnaBtn
                        ),
                        modifier = Modifier
                            .width(140.dp)
                            .align(Alignment.CenterHorizontally),
                        elevation = ButtonDefaults.buttonElevation(4.dp)
                    ) {
                        Text(
                            text = "Mulai",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}