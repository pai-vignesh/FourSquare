package com.robosoft.foursquare.model


import com.google.gson.annotations.SerializedName

data class Tip(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("text")
    val text: String
)