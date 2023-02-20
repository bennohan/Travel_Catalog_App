package com.example.travelcatalogapp.data


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ImageSlide(
    @Expose
    @SerializedName("image")
    val image: String?
)