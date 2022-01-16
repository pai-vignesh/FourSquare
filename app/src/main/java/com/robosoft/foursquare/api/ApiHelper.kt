package com.robosoft.foursquare.api

import javax.inject.Inject

class ApiHelper @Inject constructor(private val apiService: ApiService) {
    suspend fun getNearbyPlaces(ll:String) = apiService.getNearbyPlaces(ll)
    suspend fun getPlacePhotos(id:String) = apiService.getPlacePhotos(id)
    suspend fun getPlaceDetails(id: String) = apiService.getPlaceDetails(id)
}