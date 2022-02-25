package com.robosoft.foursquare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.robosoft.foursquare.repository.RoomRepository
import com.robosoft.foursquare.room.UserModel
import com.robosoft.foursquare.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val repository: RoomRepository) : ViewModel() {

    fun signInUser(email:String,password:String) = liveData(Dispatchers.IO) {
        emit(Resource.Loading)
        try {
            emit(Resource.Success(data = repository.signInUser(email,password)))
        } catch (exception: Exception) {
            emit(Resource.Error(exception))
        }
    }

    fun updatePassword(password: String,phone:String) = liveData(Dispatchers.IO) {
        emit(Resource.Loading)
        try {
            emit(Resource.Success(data = repository.updatePassword(password,phone)))
        } catch (exception: Exception) {
            emit(Resource.Error(exception))
        }
    }

    fun getUserData(email: String) = liveData(Dispatchers.IO) {
        emit(Resource.Loading)
        try {
            emit(Resource.Success(data = repository.getUserData(email)))
        } catch (exception: Exception) {
            emit(Resource.Error(exception))
        }
    }

    fun registerUser(userModel: UserModel) = liveData(Dispatchers.IO) {
        emit(Resource.Loading)
        try {
            emit(Resource.Success(data = repository.registerUser(userModel)))
        } catch (exception: Exception) {
            emit(Resource.Error(exception))
        }
    }
}