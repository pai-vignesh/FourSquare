package com.robosoft.foursquare.model


import com.google.gson.annotations.SerializedName

data class SocialMedia(
    @SerializedName("facebook_id")
    val facebookId: String,
    @SerializedName("instagram")
    val instagram: String,
    @SerializedName("twitter")
    val twitter: String
)