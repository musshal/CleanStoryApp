package com.dicoding.storyapp.core.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.dicoding.storyapp.core.data.source.local.entity.StoryEntity

interface IStoryPagingRepository {
    fun getAllStories(token: String): LiveData<PagingData<StoryEntity>>
}