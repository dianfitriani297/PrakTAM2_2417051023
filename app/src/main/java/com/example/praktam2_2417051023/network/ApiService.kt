package com.example.praktam2_2417051023.network

import com.example.praktam2_2417051023.model.MitosFaktaHewan
import retrofit2.http.GET

interface ApiService {
    @GET("hewan.json")
    suspend fun getHewan(): List<MitosFaktaHewan>
}