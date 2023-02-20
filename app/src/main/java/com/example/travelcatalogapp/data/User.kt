package com.example.travelcatalogapp.data


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class User(


    @Expose
    @SerializedName("id")
    val id: Int?,
    @Expose
    @SerializedName("image")
    val image: String?,
    @Expose
    @SerializedName("name")
    val name: String?,
    @Expose
    @SerializedName("phone_number")
    val phoneNumber: String?,
    @Expose
    @SerializedName("created_at")
    val createdAt: String?,
    @Expose
    @SerializedName("updated_at")
    val updatedAt: String?
)