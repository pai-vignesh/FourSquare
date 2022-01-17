package com.robosoft.foursquare.model

import com.google.gson.annotations.SerializedName

data class PlaceList(
    @SerializedName("results")
    val results : List<PlaceData>
)
