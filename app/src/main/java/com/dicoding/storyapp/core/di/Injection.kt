package com.dicoding.storyapp.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.dicoding.storyapp.core.data.repository.StoryPagingRepository
import com.dicoding.storyapp.core.data.repository.StoryRepository
//import com.dicoding.storyapp.core.data.repository.StoryRepository
import com.dicoding.storyapp.core.data.repository.UserRepository
import com.dicoding.storyapp.core.data.source.local.LocalDataSource
import com.dicoding.storyapp.core.data.source.local.datastore.SettingPreferences
import com.dicoding.storyapp.core.data.source.local.datastore.UserPreferences
import com.dicoding.storyapp.core.data.source.local.room.StoryDatabase
import com.dicoding.storyapp.core.data.source.remote.RemoteDataSource
import com.dicoding.storyapp.core.data.source.remote.network.ApiConfig
import com.dicoding.storyapp.core.di.Injection.userDataStore
import com.dicoding.storyapp.core.domain.usecase.story.StoryInteractor
import com.dicoding.storyapp.core.domain.usecase.story.StoryUseCase
import com.dicoding.storyapp.core.domain.usecase.user.UserInteractor
import com.dicoding.storyapp.core.domain.usecase.user.UserUseCase
import com.dicoding.storyapp.core.utils.AppExecutors

object Injection {

    private val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(
        name = "user_preferences"
    )
    private val Context.settingDataStore: DataStore<Preferences> by preferencesDataStore(
        name = "settings"
    )
    private val remoteDataSource = RemoteDataSource.getInstance(ApiConfig.provideApiService())
    private val appExecutors = AppExecutors()

    fun provideUserPreferences(context: Context) : UserPreferences {
        val dataStore = context.userDataStore

        return UserPreferences.getInstance(dataStore)
    }

    fun provideSettingPreferences(context: Context) : SettingPreferences {
        val dataStore = context.settingDataStore

        return SettingPreferences.getInstance(dataStore)
    }

    private fun provideUserRepository(context: Context) : UserRepository {
        val dataStore = context.userDataStore
        val userPreferences = UserPreferences.getInstance(dataStore)
        val storyDatabase = StoryDatabase.getInstance(context)
        val localDataSource = LocalDataSource.getInstance(userPreferences, storyDatabase.storyDao())

        return UserRepository.getInstance(remoteDataSource, localDataSource)
    }

    private fun provideStoryRepository(context: Context) : StoryRepository {
        val dataStore = context.userDataStore
        val userPreferences = UserPreferences.getInstance(dataStore)
        val storyDatabase = StoryDatabase.getInstance(context)
        val localDataSource = LocalDataSource.getInstance(userPreferences, storyDatabase.storyDao())

        return StoryRepository.getInstance(
            remoteDataSource,
            localDataSource,
            appExecutors
        )
    }

    fun provideStoryPagingRepository(context: Context): StoryPagingRepository {
        val apiService = ApiConfig.provideApiService()
        val storyDatabase = StoryDatabase.getInstance(context)

        return StoryPagingRepository.getInstance(
            apiService,
            storyDatabase
        )
    }

    fun provideUserUseCase(context: Context): UserUseCase {
        val userRepository = provideUserRepository(context)

        return UserInteractor(userRepository)
    }

    fun provideStoryUseCase(context: Context): StoryUseCase {
        val storyRepository = provideStoryRepository(context)

        return StoryInteractor(storyRepository)
    }
}