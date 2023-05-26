package com.dicoding.storyapp.core.domain.usecase.user

import androidx.lifecycle.LiveData
import com.dicoding.storyapp.core.data.repository.UserRepository
import com.dicoding.storyapp.core.data.source.local.entity.UserEntity
import com.dicoding.storyapp.core.data.source.remote.network.ApiResponse
import com.dicoding.storyapp.core.data.source.remote.request.LoginRequest
import com.dicoding.storyapp.core.data.source.remote.request.RegisterRequest
import com.dicoding.storyapp.core.data.source.remote.response.LoginResponse
import com.dicoding.storyapp.core.data.source.remote.response.MessageResponse
import kotlinx.coroutines.flow.Flow

class UserInteractor(private val userRepository: UserRepository): UserUseCase {
    override fun register(registerRequest: RegisterRequest):
            LiveData<ApiResponse<MessageResponse>> = userRepository.register(registerRequest)
    override fun login(loginRequest: LoginRequest): LiveData<ApiResponse<LoginResponse>> =
        userRepository.login(loginRequest)
    override suspend fun setLogin(userEntity: UserEntity) = userRepository.setLogin(userEntity)
    override fun getLogin(): Flow<UserEntity> = userRepository.getLogin()
    override suspend fun deleteLogin() = userRepository.deleteLogin()
}