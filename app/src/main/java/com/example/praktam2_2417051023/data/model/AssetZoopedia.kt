package com.example.praktam2_2417051023.data.model

import com.google.gson.annotations.SerializedName

data class AssetZoopedia(
    @SerializedName("logo_url")
    val logoUrl: String,

    @SerializedName("welcome_bg")
    val welcomeBg: String
)