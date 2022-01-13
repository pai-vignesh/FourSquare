package com.robosoft.foursquare.model


import com.google.gson.annotations.SerializedName

data class Chain(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
)