package com.example.praktam2_2417051023.data.model

import com.google.gson.annotations.SerializedName

data class Pertanyaan(

    @SerializedName("pertanyaan")
    val pertanyaan: String = "",

    @SerializedName("jawaban_benar")
    val jawabanBenar: String = "",

    @SerializedName("penjelasan")
    val penjelasan: String = ""
)