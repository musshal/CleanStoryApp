package com.dicoding.storyapp.core.domain.usecase.storypaging

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.dicoding.storyapp.core.data.repository.StoryPagingRepository
import com.dicoding.storyapp.core.data.source.local.entity.StoryEntity

class StoryPagingInteractor(
    private val storyPagingRepository: StoryPagingRepository
    ): StoryPagingUseCase {
    override fun getAllStories(token: String): LiveData<PagingData<StoryEntity>> =
        storyPagingRepository.getAllStories(token)
}