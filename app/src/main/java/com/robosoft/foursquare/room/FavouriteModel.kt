package com.robosoft.foursquare.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourites")
data class FavouriteModel(
    @PrimaryKey
    @ColumnInfo(name = "fsq_id")
    var fsqId: String,

    @ColumnInfo(name = "place_name")
    var placeName: String,

    @ColumnInfo(name = "place_type")
    val placeType: String,

    @ColumnInfo(name = "price")
    val price: String,

    @ColumnInfo(name = "latitude")
    val lat: String,

    @ColumnInfo(name = "longitude")
    val lng: String,

    @ColumnInfo(name = "ratings")
    val rating: String,

    @ColumnInfo(name = "placeImg")
    val placeImg: String,

    @ColumnInfo(name = "address")
    val address: String,
)
