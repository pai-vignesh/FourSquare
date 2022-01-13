package com.robosoft.foursquare.di

import android.content.Context
import com.robosoft.foursquare.api.ApiService
import com.robosoft.foursquare.api.RetrofitInstance
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

//    @Provides
//    fun provideRoomInstance(@ApplicationContext context: Context): FavouritesDao {
//        return RoomFavouritesDatabase.getInstance(context).favouritesDao
//    }

}