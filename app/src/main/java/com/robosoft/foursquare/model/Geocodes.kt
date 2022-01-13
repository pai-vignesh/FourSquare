package com.robosoft.foursquare.model


import com.google.gson.annotations.SerializedName

data class Geocodes(
    @SerializedName("drop_off")
    val dropOff: DropOff,
    @SerializedName("front_door")
    val frontDoor: FrontDoor,
    @SerializedName("main")
    val main: Main,
    @SerializedName("road")
    val road: Road,
    @SerializedName("roof")
    val roof: Roof
)