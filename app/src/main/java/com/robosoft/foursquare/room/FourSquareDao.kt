package com.robosoft.foursquare.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FourSquareDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun registerUser(userModel: UserModel): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavourite(favouriteModel: FavouriteModel): Long

    @Query("UPDATE userdata SET password=:password WHERE user_phone=:phone")
    fun updatePassword(password: String, phone: String)

    @Query("SELECT * FROM userdata WHERE email=:email LIMIT 1")
    fun getUserData(email: String): UserModel

    @Query("SELECT * FROM userdata WHERE email=:email AND password=:password LIMIT 1 ")
    fun signInUser(email: String, password: String): UserModel

    @Query("SELECT * FROM favourites")
    fun getFavourites(): LiveData<List<FavouriteModel>>

    @Delete
    suspend fun deleteFavourites(favourite: FavouriteModel)
}