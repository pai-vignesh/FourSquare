package com.robosoft.foursquare.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FourSquareDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun registerUser(userModel: UserModel): Long

    @Query("SELECT * FROM userdata WHERE email=:email AND password=:password LIMIT 1 ")
    fun signInUser(email:String,password:String): UserModel
}