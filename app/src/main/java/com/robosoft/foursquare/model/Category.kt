package com.robosoft.foursquare.model


import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("icon")
    val icon: Icon,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
)