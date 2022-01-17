package com.robosoft.foursquare.model


import com.google.gson.annotations.SerializedName

data class Regular(
    @SerializedName("close")
    val close: String,
    @SerializedName("day")
    val day: String,
    @SerializedName("open")
    val `open`: String
)