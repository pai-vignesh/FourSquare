package com.robosoft.foursquare.api

import com.robosoft.foursquare.BuildConfig
import com.robosoft.foursquare.helper.Constants
import com.robosoft.foursquare.model.Photo
import com.robosoft.foursquare.model.PlaceData
import com.robosoft.foursquare.model.PlaceList
import com.robosoft.foursquare.model.Tip
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @Headers(
        "Authorization: ${BuildConfig.API_KEY}",
        "Accept: application/json"
    )
    @GET(Constants.END_POINT)
    suspend fun getNearbyPlaces(@Query("ll") query: String): PlaceList

    @Headers(
        "Authorization: ${BuildConfig.API_KEY}",
        "Accept: application/json"
    )
    @GET(Constants.SEARCH_POINT)
    suspend fun getQueryPlaces(
        @Query("query") query: String,
        @Query("ll") ll: String,
        @Query("fields") fields: String,
        @Query("sort") sort: String
    ): PlaceList

    @Headers(
        "Authorization: ${BuildConfig.API_KEY}",
        "Accept: application/json"
    )
    @GET(Constants.SEARCH_POINT)
    suspend fun getSearchPlaces(
        @Query("query") query: String,
        @Query("fields") fields: String,
        @Query("near") near: String
    ): PlaceList

    @Headers(
        "Authorization: ${BuildConfig.API_KEY}",
        "Accept: application/json"
    )
    @GET("places/{id}/photos")
    suspend fun getPlacePhotos(@Path("id") query: String): List<Photo>

    @Headers(
        "Authorization: ${BuildConfig.API_KEY}",
        "Accept: application/json"
    )
    @GET("places/{id}?fields=fsq_id,photos,name,price,location,categories,description,tel,geocodes,rating")
    suspend fun getPlaceDetails(@Path("id") query: String): PlaceData

    @Headers(
        "Authorization: ${BuildConfig.API_KEY}",
        "Accept: application/json"
    )
    @GET("places/{id}/tips")
    suspend fun getPlaceReviews(@Path("id") query: String): List<Tip>
}