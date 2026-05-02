package com.example.praktam2_2417051023.model

import com.google.gson.annotations.SerializedName

data class MitosFaktaHewan(
    @SerializedName("nama_hewan")
    val namaHewan: String? = null,

    @SerializedName("image_url")
    val imageUrl: String? = null,
    @SerializedName("mitos")
    val mitos: String? = null,

    @SerializedName("fakta")
    val fakta: String? = null
)