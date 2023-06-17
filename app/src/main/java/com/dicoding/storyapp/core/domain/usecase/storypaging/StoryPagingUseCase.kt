package com.dicoding.storyapp.core.domain.usecase.storypaging

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.dicoding.storyapp.core.domain.model.Story

interface StoryPagingUseCase {
    fun getAllStories(token: String): LiveData<PagingData<Story>>
}