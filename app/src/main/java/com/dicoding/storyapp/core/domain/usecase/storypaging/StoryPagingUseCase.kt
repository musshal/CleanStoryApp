package com.dicoding.storyapp.core.domain.usecase.storypaging

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.dicoding.storyapp.core.data.source.local.entity.StoryEntity

interface StoryPagingUseCase {
    fun getAllStories(token: String): LiveData<PagingData<StoryEntity>>
}