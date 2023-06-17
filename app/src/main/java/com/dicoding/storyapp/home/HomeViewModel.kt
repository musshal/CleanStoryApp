package com.dicoding.storyapp.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.storyapp.core.data.source.local.entity.StoryEntity
import com.dicoding.storyapp.core.data.source.local.entity.UserEntity
import com.dicoding.storyapp.core.domain.usecase.story.StoryUseCase
import com.dicoding.storyapp.core.domain.usecase.storypaging.StoryPagingUseCase
import com.dicoding.storyapp.core.domain.usecase.user.UserUseCase
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userUseCase: UserUseCase,
    private val storyUseCase: StoryUseCase,
    private val storyPagingUseCase: StoryPagingUseCase
    ) : ViewModel() {

    fun getLogin() : LiveData<UserEntity> = userUseCase.getLogin().asLiveData()

    fun deleteLogin() { viewModelScope.launch { userUseCase.deleteLogin() } }

    fun getAllStories(token: String): LiveData<PagingData<StoryEntity>> =
        storyPagingUseCase.getAllStories(token).cachedIn(viewModelScope)

    fun getBookmarkedStories() = storyUseCase.getBookmarkedStories()

    fun saveStory(story: StoryEntity) {
        viewModelScope.launch {
            storyUseCase.setStoryBookmark(story, true)
        }
    }

    fun deleteStory(story: StoryEntity) {
        viewModelScope.launch {
            storyUseCase.setStoryBookmark(story, false)
        }
    }
}