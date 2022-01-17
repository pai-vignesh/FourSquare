package com.robosoft.foursquare.model


import com.google.gson.annotations.SerializedName

data class RelatedPlaces(
    @SerializedName("children")
    val children: List<Children>,
    @SerializedName("parent")
    val parent: Parent
)