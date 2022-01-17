package com.robosoft.foursquare.model


import com.google.gson.annotations.SerializedName

data class Hours(
    @SerializedName("display")
    val display: String,
    @SerializedName("is_local_holiday")
    val isLocalHoliday: String,
    @SerializedName("open_now")
    val openNow: String,
    @SerializedName("regular")
    val regular: List<Regular>,
    @SerializedName("seasonal")
    val seasonal: List<Seasonal>
)