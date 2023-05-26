package com.dicoding.storyapp.core.domain.repository

import androidx.lifecycle.LiveData
import com.dicoding.storyapp.core.data.source.local.entity.UserEntity
import com.dicoding.storyapp.core.data.source.remote.network.ApiResponse
import com.dicoding.storyapp.core.data.source.remote.request.LoginRequest
import com.dicoding.storyapp.core.data.source.remote.request.RegisterRequest
import com.dicoding.storyapp.core.data.source.remote.response.LoginResponse
import com.dicoding.storyapp.core.data.source.remote.response.MessageResponse
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    fun register(registerRequest: RegisterRequest): LiveData<ApiResponse<MessageResponse>>
    fun login(loginRequest: LoginRequest): LiveData<ApiResponse<LoginResponse>>
    suspend fun setLogin(userEntity: UserEntity)
    fun getLogin(): Flow<UserEntity>
    suspend fun deleteLogin()
}