package com.example.praktam2_2417051023.data.repository

import com.example.praktam2_2417051023.data.api.RetrofitClient
import com.example.praktam2_2417051023.data.model.KategoriHewan

class KategoriRepository {
    suspend fun getKategori(): List<KategoriHewan> {
        return try {
            RetrofitClient.instance.getKategori()
        } catch (_: Exception) {
            emptyList()
        }
    }
}