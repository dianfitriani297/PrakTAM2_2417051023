package com.example.praktam2_2417051023.data.repository

import com.example.praktam2_2417051023.data.api.RetrofitClient
import com.example.praktam2_2417051023.data.model.AssetZoopedia

class AssetRepository {

    suspend fun getAssets(): AssetZoopedia? {
        return try {
            RetrofitClient.instance.getAssets()
        } catch (e: Exception) {
            null
        }
    }
}