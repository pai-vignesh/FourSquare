package com.robosoft.foursquare.model


import com.google.gson.annotations.SerializedName

data class Seasonal(
    @SerializedName("closed")
    val closed: String,
    @SerializedName("end_date")
    val endDate: String,
    @SerializedName("hours")
    val hours: List<Hour>,
    @SerializedName("start_date")
    val startDate: String
)