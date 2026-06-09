package com.example.praktam2_2417051023.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.praktam2_2417051023.component.BottomNavBar
import com.example.praktam2_2417051023.component.ZoopediaImage
import com.example.praktam2_2417051023.data.model.MitosFaktaHewan
import com.example.praktam2_2417051023.data.datastore.UserPreferencesRepository
import com.example.praktam2_2417051023.ui.theme.BenarBackground
import com.example.praktam2_2417051023.ui.theme.BenarColor
import com.example.praktam2_2417051023.ui.theme.BlueButton
import com.example.praktam2_2417051023.ui.theme.GreenPrimary
import com.example.praktam2_2417051023.ui.theme.GreenSoft
import com.example.praktam2_2417051023.ui.theme.OrangeMitos
import com.example.praktam2_2417051023.ui.theme.SalahBackground
import com.example.praktam2_2417051023.ui.theme.SalahColor
import kotlinx.coroutines.launch

@Composable
fun QuizPage(
    hewan: MitosFaktaHewan,
    navController: NavController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val userRepo = remember { UserPreferencesRepository(context) }

    val soalList = hewan.soal
    val totalSoal = soalList.size

    var currentIndex by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var clickedButton by remember { mutableStateOf<String?>(null) }
    var showFeedback by remember { mutableStateOf(false) }

    val soalSekarang = if (soalList.isNotEmpty()) soalList[currentIndex] else null
    val isLastSoal = currentIndex == totalSoal - 1

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavBar(navController)
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(bottom = paddingValues.calculateBottomPadding())
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
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
                    text = "Quiz ${hewan.namaHewan}",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = GreenPrimary,
                    modifier = Modifier.weight(1f)
                )

                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = GreenPrimary,
                    modifier = Modifier.padding(end = 12.dp)
                ) {
                    Text(
                        text = "${currentIndex + 1} / $totalSoal",
                        modifier = Modifier.padding(
                            horizontal = 14.dp,
                            vertical = 6.dp
                        ),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            ZoopediaImage(
                imageUrl = hewan.imageUrl,
                contentDescription = hewan.namaHewan,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            soalSekarang?.let { soal ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(3.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(50.dp),
                            color = GreenSoft
                        ) {
                            Text(
                                text = "PERTANYAAN",
                                modifier = Modifier.padding(
                                    horizontal = 12.dp,
                                    vertical = 4.dp
                                ),
                                color = GreenPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = soal.pertanyaan,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val mitosBackground = when (clickedButton) {
                        null -> OrangeMitos
                        "Mitos" -> OrangeMitos
                        else -> Color(0xFFFFCCBC)
                    }

                    Button(
                        onClick = {
                            if (clickedButton == null) {
                                clickedButton = "Mitos"

                                if (soal.jawabanBenar == "Mitos") {
                                    score++
                                }

                                showFeedback = true
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        enabled = clickedButton == null,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = mitosBackground,
                            disabledContainerColor = mitosBackground
                        )
                    ) {
                        Text(
                            text = "Mitos",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }

                    val faktaBackground = when (clickedButton) {
                        null -> BlueButton
                        "Fakta" -> BlueButton
                        else -> Color(0xFFBBDEFB)
                    }

                    Button(
                        onClick = {
                            if (clickedButton == null) {
                                clickedButton = "Fakta"

                                if (soal.jawabanBenar == "Fakta") {
                                    score++
                                }

                                showFeedback = true
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        enabled = clickedButton == null,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = faktaBackground,
                            disabledContainerColor = faktaBackground
                        )
                    ) {
                        Text(
                            text = "Fakta",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (showFeedback) {
                    val isBenar = clickedButton == soal.jawabanBenar
                    val borderColor = if (isBenar) BenarColor else SalahColor
                    val backgroundColor = if (isBenar) BenarBackground else SalahBackground
                    val feedbackText = if (isBenar) "Benar!" else "Salah!"
                    val feedbackIcon = if (isBenar) {
                        Icons.Filled.CheckCircle
                    } else {
                        Icons.Filled.Cancel
                    }
                    val iconColor = if (isBenar) BenarColor else SalahColor

                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = backgroundColor
                            ),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = feedbackIcon,
                                        contentDescription = null,
                                        tint = iconColor,
                                        modifier = Modifier.size(32.dp)
                                    )

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Column {
                                        Text(
                                            text = feedbackText,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            color = borderColor
                                        )

                                        Text(
                                            text = "Itu adalah ${soal.jawabanBenar}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = borderColor.copy(alpha = 0.8f),
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                HorizontalDivider(
                                    color = borderColor.copy(alpha = 0.3f)
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = soal.penjelasan,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF333333)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                if (isLastSoal) {
                                    scope.launch {
                                        val nama = hewan.namaHewan

                                        userRepo.setSelesai(nama)
                                        userRepo.updateSkor(nama, score)

                                        navController.navigate(
                                            "result/$nama/$score/$totalSoal"
                                        ) {
                                            popUpTo("quiz/$nama") {
                                                inclusive = true
                                            }
                                        }
                                    }
                                } else {
                                    currentIndex++
                                    clickedButton = null
                                    showFeedback = false
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GreenPrimary
                            )
                        ) {
                            Text(
                                text = if (isLastSoal) {
                                    "Lihat Hasil"
                                } else {
                                    "Soal Berikutnya"
                                },
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}