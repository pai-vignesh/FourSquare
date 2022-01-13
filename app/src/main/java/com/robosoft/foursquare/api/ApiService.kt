package com.robosoft.foursquare.api

import com.robosoft.foursquare.helper.Constants
import com.robosoft.foursquare.model.PlaceList
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {

    @Headers(
        "Authorization: fsq3zwV1cGTqlpvaBC7udG4trZkuAe5SFxmUWAeqeaHNwHw=",
        "Accept: application/json"
    )
    @GET(Constants.END_POINT)
    suspend fun getNearbyPlaces(@Query("ll") query: String): PlaceList

//    @Headers(
//        "Authorization: 563492ad6f91700001000001a7e4caea8a2a4ec1b4b5fd16e48280cf",
//        "Accept: application/json"
//    )
//    @GET(Constants.VIDEO_END_POINT)
//    suspend fun getVideos(): Videos
//
//    @Headers(
//        "Authorization: 563492ad6f91700001000001a7e4caea8a2a4ec1b4b5fd16e48280cf",
//        "Accept: application/json"
//    )
//    @GET(Constants.SEARCH_POINT)
//    suspend fun searchPhotos(@Query("query") query: String): Photos
}