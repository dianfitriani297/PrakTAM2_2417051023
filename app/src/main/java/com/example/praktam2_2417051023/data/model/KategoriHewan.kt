package com.example.praktam2_2417051023.data.model

import com.google.gson.annotations.SerializedName

data class KategoriHewan(
    @SerializedName("nama_kategori")
    val namaKategori: String = "",

    @SerializedName("image_url")
    val imageUrl: String = "",

    @SerializedName("deskripsi")
    val deskripsi: String = "",

    @SerializedName("contoh")
    val contoh: List<String> = emptyList()
)