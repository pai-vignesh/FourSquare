package com.robosoft.foursquare.repository

import com.robosoft.foursquare.api.ApiHelper
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiHelper: ApiHelper) {
    suspend fun getNearbyPlaces(ll: String) = apiHelper.getNearbyPlaces(ll)
    suspend fun getPlacePhotos(id: String) = apiHelper.getPlacePhotos(id)
    suspend fun getPlaceDetails(id: String) = apiHelper.getPlaceDetails(id)
    suspend fun getQueryPlaces(query: String, ll: String, fields: String, sort: String) =
        apiHelper.getQueryPlaces(query, ll, fields, sort)

    suspend fun getPlaceReviews(id: String) = apiHelper.getPlaceReviews(id)
    suspend fun getSearchPlace(query: String,fields: String, near: String) = apiHelper.getSearchPlace(query,fields,near)
}