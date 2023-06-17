package com.dicoding.storyapp.core.data.repository

import androidx.lifecycle.LiveData
import com.dicoding.storyapp.core.data.source.local.LocalDataSource
import com.dicoding.storyapp.core.data.source.local.entity.UserEntity
import com.dicoding.storyapp.core.data.source.remote.RemoteDataSource
import com.dicoding.storyapp.core.data.source.remote.network.ApiResponse
import com.dicoding.storyapp.core.data.source.remote.request.LoginRequest
import com.dicoding.storyapp.core.data.source.remote.request.RegisterRequest
import com.dicoding.storyapp.core.data.source.remote.response.LoginResponse
import com.dicoding.storyapp.core.data.source.remote.response.MessageResponse
import com.dicoding.storyapp.core.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
    ): IUserRepository {

    override fun register(registerRequest: RegisterRequest):
            LiveData<ApiResponse<MessageResponse>> = remoteDataSource.register(registerRequest)

    override fun login(loginRequest: LoginRequest): LiveData<ApiResponse<LoginResponse>> =
        remoteDataSource.login(loginRequest)

    override suspend fun setLogin(userEntity: UserEntity) = localDataSource.setLogin(userEntity)

    override fun getLogin(): Flow<UserEntity> = localDataSource.getLogin()

    override suspend fun deleteLogin() = localDataSource.deleteLogin()

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            remoteDataSource: RemoteDataSource,
            localDataSource: LocalDataSource
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(remoteDataSource, localDataSource)
            }.also { instance = it }
    }
}