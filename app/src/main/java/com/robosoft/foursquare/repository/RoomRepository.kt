package com.robosoft.foursquare.repository

import com.robosoft.foursquare.room.FavouriteModel
import com.robosoft.foursquare.room.FourSquareDao
import com.robosoft.foursquare.room.UserModel
import javax.inject.Inject

class RoomRepository @Inject constructor(private val dao: FourSquareDao) {

    suspend fun registerUser(userModel: UserModel): Long =
        dao.registerUser(userModel)

    suspend fun signInUser(email:String,password:String) =
        dao.signInUser(email,password)

    suspend fun deleteFavourites(favourites: FavouriteModel){
        dao.deleteFavourites(favourites)
    }

    suspend fun addFavourites(favourites: FavouriteModel): Long=
        dao.addFavourite(favourites)


    val favourites = dao.getFavourites()


}