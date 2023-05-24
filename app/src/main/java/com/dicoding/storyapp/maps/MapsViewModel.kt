package com.dicoding.storyapp.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.core.data.repository.StoryRepository
import com.dicoding.storyapp.core.data.source.local.datastore.UserPreferences
import com.dicoding.storyapp.core.data.source.local.entity.UserEntity
import kotlinx.coroutines.launch

class MapsViewModel(
    private val userPreferences: UserPreferences,
    private val storyRepository: StoryRepository
    ): ViewModel() {

    fun getLogin(): LiveData<UserEntity> = userPreferences.getLogin().asLiveData()

    fun deleteLogin() { viewModelScope.launch { userPreferences.deleteLogin() } }

    fun getAllStoriesWithLocation(token: String) =
        storyRepository.getAllStoriesWithLocation(token)
}