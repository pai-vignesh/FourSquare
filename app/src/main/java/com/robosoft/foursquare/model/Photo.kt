package com.robosoft.foursquare.model


import com.google.gson.annotations.SerializedName

data class Photo(
    @SerializedName("classifications")
    val classifications: List<String>,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("height")
    val height: String,
    @SerializedName("prefix")
    val prefix: String,
    @SerializedName("suffix")
    val suffix: String,
    @SerializedName("width")
    val width: String
)