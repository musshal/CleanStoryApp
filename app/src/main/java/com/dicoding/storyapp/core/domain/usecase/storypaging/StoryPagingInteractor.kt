package com.dicoding.storyapp.core.domain.usecase.storypaging

import androidx.paging.PagingData
import com.dicoding.storyapp.core.data.repository.StoryPagingRepository
import com.dicoding.storyapp.core.domain.model.Story
import kotlinx.coroutines.flow.Flow

class StoryPagingInteractor(
    private val storyPagingRepository: StoryPagingRepository
    ): StoryPagingUseCase {
    override fun getAllStories(token: String): Flow<PagingData<Story>> =
        storyPagingRepository.getAllStories(token)
}