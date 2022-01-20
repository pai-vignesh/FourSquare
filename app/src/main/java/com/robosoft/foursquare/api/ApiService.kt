package com.robosoft.foursquare.api

import com.robosoft.foursquare.helper.Constants
import com.robosoft.foursquare.model.Photo
import com.robosoft.foursquare.model.PlaceData
import com.robosoft.foursquare.model.PlaceList
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @Headers(
        "Authorization: fsq3zwV1cGTqlpvaBC7udG4trZkuAe5SFxmUWAeqeaHNwHw=",
        "Accept: application/json"
    )
    @GET(Constants.END_POINT)
    suspend fun getNearbyPlaces(@Query("ll") query: String): PlaceList

    @Headers(
        "Authorization: fsq3zwV1cGTqlpvaBC7udG4trZkuAe5SFxmUWAeqeaHNwHw=",
        "Accept: application/json"
    )
    @GET(Constants.SEARCH_POINT)
    suspend fun getQueryPlaces(@Query("query") query:String, @Query("ll") ll: String,@Query("fields") fields:String,@Query("sort") sort:String): PlaceList

    @Headers(
        "Authorization: fsq3zwV1cGTqlpvaBC7udG4trZkuAe5SFxmUWAeqeaHNwHw=",
        "Accept: application/json"
    )
    @GET("places/{id}/photos")
    suspend fun getPlacePhotos(@Path("id") query: String): List<Photo>

    @Headers(
        "Authorization: fsq3zwV1cGTqlpvaBC7udG4trZkuAe5SFxmUWAeqeaHNwHw=",
        "Accept: application/json"
    )
    @GET("places/{id}?fields=fsq_id,photos,name,price,location,categories,description,tel,geocodes,rating")
    suspend fun getPlaceDetails(@Path("id") query: String): PlaceData



}