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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.praktam2_2417051023.model.MitosFaktaHewan
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

fun dapatkanWarnaTombol(warnaCard: Color): Color {
    return when (warnaCard) {
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
    val warnaWarni = listOf(
        Color(0xFFE3F2FD), Color(0xFFFFF3E0), Color(0xFFE8F5E9),
        Color(0xFFFCE4EC), Color(0xFFF3E5F5)
    )

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
            modifier = Modifier.padding(bottom = 24.dp)
        )

        MitosFaktaHewanSource.daftarMitosFaktaHewan.forEachIndexed { index, hewan ->
            val warnaCard = warnaWarni[index % warnaWarni.size]
            val warnaTombol = dapatkanWarnaTombol(warnaCard)

            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = warnaCard),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Image(
                        painter = painterResource(id = hewan.imageRes),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(170.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.Crop
                    )

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
                            containerColor = warnaTombol
                        ),
                        modifier = Modifier
                            .width(140.dp)
                            .align(Alignment.CenterHorizontally),
                        elevation = ButtonDefaults.buttonElevation(4.dp)
                    ) {
                        Text(text = "Mulai", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}