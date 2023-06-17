package com.dicoding.storyapp.core.domain.usecase.story

import androidx.lifecycle.LiveData
import com.dicoding.storyapp.core.data.repository.StoryRepository
import com.dicoding.storyapp.core.data.source.local.entity.StoryEntity
import com.dicoding.storyapp.core.data.source.remote.network.ApiResponse
import com.dicoding.storyapp.core.data.source.remote.request.NewStoryRequest
import com.dicoding.storyapp.core.data.source.remote.response.AllStoriesResponse
import com.dicoding.storyapp.core.data.source.remote.response.DetailStoryResponse
import com.dicoding.storyapp.core.data.source.remote.response.MessageResponse
import com.dicoding.storyapp.core.domain.model.Story

class StoryInteractor(private val storyRepository: StoryRepository): StoryUseCase {
    override fun addNewStory(
        token: String?,
        newStoryRequest: NewStoryRequest
    ): LiveData<ApiResponse<MessageResponse>> = storyRepository.addNewStory(token, newStoryRequest)

    override fun getBookmarkedStories(): LiveData<List<Story>> =
        storyRepository.getBookmarkedStories()
    override fun setStoryBookmark(story: Story, bookmarkState: Boolean) =
        storyRepository.setStoryBookmark(story, bookmarkState)
    override fun getDetailStory(token: String, id: String): LiveData<ApiResponse<DetailStoryResponse>> =
        storyRepository.getDetailStory(token, id)

    override fun getAllStoriesWithLocation(token: String): LiveData<ApiResponse<AllStoriesResponse>> =
        storyRepository.getAllStoriesWithLocation(token)
}