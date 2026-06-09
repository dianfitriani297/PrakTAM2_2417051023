package com.example.praktam2_2417051023.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil.compose.SubcomposeAsyncImage
import com.example.praktam2_2417051023.ui.theme.GreenPrimary
import com.example.praktam2_2417051023.ui.theme.GreenSoft

@Composable
fun ZoopediaImage(
    imageUrl: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    alignment: Alignment = Alignment.Center
) {
    SubcomposeAsyncImage(
        model = imageUrl,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
        alignment = alignment,
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(GreenSoft),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = GreenPrimary
                )
            }
        },
        error = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(GreenSoft),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Gambar gagal dimuat",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    )
}