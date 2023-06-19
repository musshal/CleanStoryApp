package com.dicoding.storyapp.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.dicoding.storyapp.BuildConfig
import com.dicoding.storyapp.core.data.repository.StoryPagingRepository
import com.dicoding.storyapp.core.data.repository.StoryRepository
import com.dicoding.storyapp.core.data.repository.UserRepository
import com.dicoding.storyapp.core.data.source.local.LocalDataSource
import com.dicoding.storyapp.core.data.source.local.datastore.SettingPreferences
import com.dicoding.storyapp.core.data.source.local.datastore.UserPreferences
import com.dicoding.storyapp.core.data.source.local.room.StoryDatabase
import com.dicoding.storyapp.core.data.source.remote.RemoteDataSource
import com.dicoding.storyapp.core.data.source.remote.network.ApiService
import com.dicoding.storyapp.core.domain.repository.IStoryPagingRepository
import com.dicoding.storyapp.core.domain.repository.IStoryRepository
import com.dicoding.storyapp.core.domain.repository.IUserRepository
import com.dicoding.storyapp.core.utils.AppExecutors
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "user_preferences"
)

private val Context.settingDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "setting_preferences"
)

val datastoreModule = module {
    single { androidContext().userDataStore }
    single { androidContext().settingDataStore }
}

val preferencesModule = module {
    single { UserPreferences(get()) }
    single { SettingPreferences(get()) }
}

val databaseModule = module {
    factory { get<StoryDatabase>().storyDao() }
    factory { get<StoryDatabase>().remoteKeysDao() }
    single {
        Room.databaseBuilder(
            androidContext(),
            StoryDatabase::class.java, "stories_db"
        ).fallbackToDestructiveMigration().build()
    }
}

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.STORY_API)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single { LocalDataSource(get(), get()) }
    single { RemoteDataSource(get()) }
    factory { AppExecutors() }
    single<IUserRepository> { UserRepository(get(), get()) }
    single<IStoryRepository> {StoryRepository(get(), get(), get())}
    single<IStoryPagingRepository> { StoryPagingRepository(get(), get()) }
}