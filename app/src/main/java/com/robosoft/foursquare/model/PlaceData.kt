package com.robosoft.foursquare.model


import com.google.gson.annotations.SerializedName

data class PlaceData(
    @SerializedName("categories")
    val categories: List<Category>,
    @SerializedName("chains")
    val chains: List<Chain>,
    @SerializedName("date_closed")
    val dateClosed: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("distance")
    val distance: Int,
    @SerializedName("email")
    val email: String,
    @SerializedName("fax")
    val fax: String,
    @SerializedName("fsq_id")
    val fsqId: String,
    @SerializedName("geocodes")
    val geocodes: Geocodes,
    @SerializedName("hours")
    val hours: Hours,
    @SerializedName("hours_popular")
    val hoursPopular: List<HoursPopular>,
    @SerializedName("location")
    val location: Location,
    @SerializedName("menu")
    val menu: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("photos")
    val photos: List<Photo>,
    @SerializedName("popularity")
    val popularity: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("rating")
    val rating: String,
    @SerializedName("related_places")
    val relatedPlaces: RelatedPlaces,
    @SerializedName("social_media")
    val socialMedia: SocialMedia,
    @SerializedName("stats")
    val stats: Stats,
    @SerializedName("tastes")
    val tastes: List<String>,
    @SerializedName("tel")
    val tel: String,
    @SerializedName("timezone")
    val timezone: String,
    @SerializedName("tips")
    val tips: List<Tip>,
    @SerializedName("verified")
    val verified: String,
    @SerializedName("website")
    val website: String
)