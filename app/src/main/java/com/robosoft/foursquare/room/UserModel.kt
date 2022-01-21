package com.robosoft.foursquare.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userdata")
data class UserModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    var id: Long,

    @ColumnInfo(name = "user_phone")
    var phone: String,

    @ColumnInfo(name = "email")
    var email: String,

    @ColumnInfo(name = "password")
    val password: String,
)
