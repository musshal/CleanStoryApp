package com.dicoding.storyapp.core.domain.usecase.story

import androidx.lifecycle.LiveData
import com.dicoding.storyapp.core.data.repository.StoryRepository
import com.dicoding.storyapp.core.data.source.remote.network.ApiResponse
import com.dicoding.storyapp.core.data.source.remote.request.NewStoryRequest
import com.dicoding.storyapp.core.data.source.remote.response.MessageResponse

class StoryInteractor(private val storyRepository: StoryRepository): StoryUseCase {
    override fun addNewStory(
        token: String?,
        newStoryRequest: NewStoryRequest
    ): LiveData<ApiResponse<MessageResponse>> = storyRepository.addNewStory(token, newStoryRequest)
//    override fun getDetailStory(): LiveData<Resource<Story>> = storyRepository
}