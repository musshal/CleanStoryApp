package com.dicoding.storyapp.core.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.dicoding.storyapp.core.data.source.local.datastore.SettingPreferences
import com.dicoding.storyapp.core.data.source.local.datastore.UserPreferences
import com.dicoding.storyapp.core.di.Injection
import com.dicoding.storyapp.core.domain.usecase.story.StoryUseCase
import com.dicoding.storyapp.core.domain.usecase.storypaging.StoryPagingUseCase
import com.dicoding.storyapp.core.domain.usecase.user.UserUseCase
import com.dicoding.storyapp.detail.DetailViewModel
import com.dicoding.storyapp.home.HomeViewModel
import com.dicoding.storyapp.insert.InsertViewModel
import com.dicoding.storyapp.main.MainViewModel
import com.dicoding.storyapp.setting.SettingViewModel

class ViewModelFactory(
    private val userPreferences: UserPreferences,
    private val settingPreferences: SettingPreferences,
    private val userUseCase: UserUseCase,
    private val storyUseCase: StoryUseCase,
    private val storyPagingUseCase: StoryPagingUseCase
    ) : NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context) : ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideUserPreferences(context),
                    Injection.provideSettingPreferences(context),
                    Injection.provideUserUseCase(context),
                    Injection.provideStoryUseCase(context),
                    Injection.provideStoryPagingUseCase(context)
                )
            }.also { instance = it }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userUseCase) as T
            }
            modelClass.isAssignableFrom(InsertViewModel::class.java) -> {
                InsertViewModel(userUseCase, storyUseCase) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(userUseCase, storyUseCase) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(userUseCase, storyUseCase, storyPagingUseCase) as T
            }
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> {
                SettingViewModel(userPreferences, settingPreferences) as T
            }
//            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
//                MapsViewModel(userPreferences, storyRepository) as T
//            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}