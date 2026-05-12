package com.example.praktam2_2417051023.data.api

import com.example.praktam2_2417051023.data.model.MitosFaktaHewan
import retrofit2.http.GET

interface ApiService {
    @GET("hewan.json")
    suspend fun getHewan(): List<MitosFaktaHewan>
}