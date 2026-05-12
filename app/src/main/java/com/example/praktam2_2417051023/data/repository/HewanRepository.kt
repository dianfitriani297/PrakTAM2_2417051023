package com.example.praktam2_2417051023.data.repository

import com.example.praktam2_2417051023.data.api.RetrofitClient
import com.example.praktam2_2417051023.data.model.MitosFaktaHewan

class HewanRepository {
    suspend fun getHewan(): List<MitosFaktaHewan> {
        return RetrofitClient.instance.getHewan()
    }
}