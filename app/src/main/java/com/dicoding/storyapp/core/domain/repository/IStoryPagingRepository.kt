package com.dicoding.storyapp.core.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.dicoding.storyapp.core.domain.model.Story

interface IStoryPagingRepository {
    fun getAllStories(token: String): LiveData<PagingData<Story>>
}