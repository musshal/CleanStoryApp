package com.dicoding.storyapp.core.domain.usecase.story

import androidx.lifecycle.LiveData
import com.dicoding.storyapp.core.data.Resource
import com.dicoding.storyapp.core.data.source.remote.network.ApiResponse
import com.dicoding.storyapp.core.data.source.remote.request.NewStoryRequest
import com.dicoding.storyapp.core.data.source.remote.response.MessageResponse
import com.dicoding.storyapp.core.domain.model.Story

interface StoryUseCase {
    fun addNewStory(
        token: String? = null,
        newStoryRequest: NewStoryRequest
    ): LiveData<ApiResponse<MessageResponse>>
//    fun getDetailStory(): LiveData<Resource<Story>>
}