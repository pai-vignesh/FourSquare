package com.robosoft.foursquare.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserModel::class,FavouriteModel::class], version = 1)
abstract class FourSquareRoomDatabase : RoomDatabase() {
    abstract val fourSquareDao: FourSquareDao

    companion object {
        @Volatile
        private var INSTANCE: FourSquareRoomDatabase? = null

        fun getInstance(context: Context): FourSquareRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FourSquareRoomDatabase::class.java,
                    "favourites_data_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}