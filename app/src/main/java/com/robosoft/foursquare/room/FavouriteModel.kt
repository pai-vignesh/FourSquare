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
    val address: String
){
    override fun equals(other: Any?): Boolean {
        if(javaClass != other?.javaClass){
            return false
        }
        other as FavouriteModel
        if(fsqId!=other.fsqId){
            return false
        }
        if(placeName!=other.placeName){
            return false
        }
        if(price!=other.price){
            return false
        }
        if(lat!=other.lat){
            return false
        }
        if(lng!=other.lng){
            return false
        }
        if(rating!=other.rating){
            return false
        }
        if(placeImg!=other.placeImg){
            return false
        }
        if(placeType!=other.placeType){
            return false
        }
        if(address!=other.address){
            return false
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = fsqId.hashCode()
        result = 31 * result + placeName.hashCode()
        result = 31 * result + placeType.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + lat.hashCode()
        result = 31 * result + lng.hashCode()
        result = 31 * result + rating.hashCode()
        result = 31 * result + placeImg.hashCode()
        result = 31 * result + address.hashCode()
        return result
    }
}
