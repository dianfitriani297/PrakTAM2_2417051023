package com.example.praktam2_2417051023.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.praktam2_2417051023.ui.theme.GreenPrimary
import com.example.praktam2_2417051023.ui.theme.OnPrimaryText
import com.example.praktam2_2417051023.ui.theme.SplashBottom
import com.example.praktam2_2417051023.ui.theme.SplashCircleGreen
import com.example.praktam2_2417051023.ui.theme.SplashCircleOrange
import com.example.praktam2_2417051023.ui.theme.SplashMiddle
import com.example.praktam2_2417051023.ui.theme.SplashSubText
import com.example.praktam2_2417051023.ui.theme.SplashTextGreen
import com.example.praktam2_2417051023.ui.theme.SplashTop

@Composable
fun SplashPage() {
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            SplashTop,
            SplashMiddle,
            SplashBottom
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        Surface(
            modifier = Modifier
                .size(245.dp)
                .offset(x = (-85).dp, y = (-75).dp)
                .blur(8.dp),
            shape = CircleShape,
            color = SplashCircleGreen.copy(alpha = 0.28f)
        ) {}

        Surface(
            modifier = Modifier
                .size(190.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 58.dp, y = 62.dp)
                .blur(9.dp),
            shape = CircleShape,
            color = SplashCircleGreen.copy(alpha = 0.24f)
        ) {}

        Surface(
            modifier = Modifier
                .size(130.dp)
                .align(Alignment.TopEnd)
                .offset(x = 40.dp, y = 130.dp)
                .blur(7.dp),
            shape = CircleShape,
            color = SplashCircleOrange.copy(alpha = 0.32f)
        ) {}

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier
                    .size(102.dp)
                    .shadow(
                        elevation = 15.dp,
                        shape = RoundedCornerShape(34.dp)
                    ),
                shape = RoundedCornerShape(34.dp),
                color = GreenPrimary
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Z",
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Bold,
                        color = OnPrimaryText
                    )
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            Text(
                text = "Zoopedia",
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold,
                color = SplashTextGreen,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Menyiapkan petualangan satwa...",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = SplashSubText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(34.dp))

            CircularProgressIndicator(
                modifier = Modifier.size(34.dp),
                color = GreenPrimary,
                strokeWidth = 3.dp
            )
        }
    }
}