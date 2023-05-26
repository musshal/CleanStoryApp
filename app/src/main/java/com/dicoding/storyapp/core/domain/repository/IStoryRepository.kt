package com.dicoding.storyapp.core.domain.repository

import androidx.lifecycle.LiveData
import com.dicoding.storyapp.core.data.Resource
import com.dicoding.storyapp.core.data.source.remote.network.ApiResponse
import com.dicoding.storyapp.core.data.source.remote.request.NewStoryRequest
import com.dicoding.storyapp.core.data.source.remote.response.MessageResponse
import com.dicoding.storyapp.core.domain.model.Story

interface IStoryRepository {
    fun addNewStory(token: String? = null, newStoryRequest: NewStoryRequest): LiveData<ApiResponse<MessageResponse>>
    fun getAllStoriesWithLocation(token: String): LiveData<Resource<List<Story>>>
//    fun getDetailStory(token: String, id: String): LiveData<ApiResponse<Story>>
    fun setStoryBookmark(story: Story, bookmarkState: Boolean)
    fun getBookmarkedStories(): LiveData<List<Story>>
}