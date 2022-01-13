package com.robosoft.foursquare.model


import com.google.gson.annotations.SerializedName

data class Stats(
    @SerializedName("total_photos")
    val totalPhotos: String,
    @SerializedName("total_ratings")
    val totalRatings: String,
    @SerializedName("total_tips")
    val totalTips: String
)