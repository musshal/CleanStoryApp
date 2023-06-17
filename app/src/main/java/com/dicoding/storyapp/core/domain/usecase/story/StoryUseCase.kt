package com.dicoding.storyapp.core.domain.usecase.story

import androidx.lifecycle.LiveData
import com.dicoding.storyapp.core.data.source.local.entity.StoryEntity
import com.dicoding.storyapp.core.data.source.remote.network.ApiResponse
import com.dicoding.storyapp.core.data.source.remote.request.NewStoryRequest
import com.dicoding.storyapp.core.data.source.remote.response.AllStoriesResponse
import com.dicoding.storyapp.core.data.source.remote.response.DetailStoryResponse
import com.dicoding.storyapp.core.data.source.remote.response.MessageResponse

interface StoryUseCase {
    fun addNewStory(
        token: String? = null,
        newStoryRequest: NewStoryRequest
    ): LiveData<ApiResponse<MessageResponse>>
    fun getBookmarkedStories(): LiveData<List<StoryEntity>>
    fun setStoryBookmark(story: StoryEntity, bookmarkState: Boolean)
    fun getDetailStory(token: String, id: String): LiveData<ApiResponse<DetailStoryResponse>>
    fun getAllStoriesWithLocation(token: String): LiveData<ApiResponse<AllStoriesResponse>>
}