package com.example.praktam2_2417051023.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Gunakan URL dasar (Base URL) yang bersih sampai bagian /raw/
    private const val BASE_URL = "https://gist.githubusercontent.com/dianfitriani297/5d2557ab20523299002551a4c2c0b21f/raw/"

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}