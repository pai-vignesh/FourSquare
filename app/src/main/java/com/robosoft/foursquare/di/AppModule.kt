package com.robosoft.foursquare.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.robosoft.foursquare.api.ApiService
import com.robosoft.foursquare.api.RetrofitInstance
import com.robosoft.foursquare.room.FourSquareDao
import com.robosoft.foursquare.room.FourSquareRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideApiService(@ApplicationContext context: Context): ApiService {
        return RetrofitInstance.getApiService(context)
    }

    @Provides
    fun provideFusedLocation(@ApplicationContext context: Context) : FusedLocationProviderClient{
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Provides
    fun provideRoomInstance(@ApplicationContext context: Context): FourSquareDao {
        return FourSquareRoomDatabase.getInstance(context).fourSquareDao
    }

}