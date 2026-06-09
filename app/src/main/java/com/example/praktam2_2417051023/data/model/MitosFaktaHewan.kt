package com.example.praktam2_2417051023.data.model

import com.google.gson.annotations.SerializedName

data class MitosFaktaHewan(

    @SerializedName("nama_hewan")
    val namaHewan: String = "",

    @SerializedName("image_url")
    val imageUrl: String = "",

    @SerializedName("soal")
    val soal: List<Pertanyaan> = emptyList()
)