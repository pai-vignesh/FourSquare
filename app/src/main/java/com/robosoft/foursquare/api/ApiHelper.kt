package com.robosoft.foursquare.api

import javax.inject.Inject

class ApiHelper @Inject constructor(private val apiService: ApiService) {
    suspend fun getNearbyPlaces(ll:String) = apiService.getNearbyPlaces(ll)
}