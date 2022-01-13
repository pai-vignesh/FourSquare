package com.robosoft.foursquare.model


import com.google.gson.annotations.SerializedName

data class Roof(
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("longitude")
    val longitude: String
)