package com.robosoft.foursquare.model


import com.google.gson.annotations.SerializedName

data class Road(
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("longitude")
    val longitude: String
)