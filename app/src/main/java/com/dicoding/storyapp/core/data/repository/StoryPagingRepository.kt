package com.dicoding.storyapp.core.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.storyapp.core.data.paging.StoryRemoteMediator
import com.dicoding.storyapp.core.data.source.local.entity.StoryEntity
import com.dicoding.storyapp.core.data.source.local.room.StoryDatabase
import com.dicoding.storyapp.core.data.source.remote.network.ApiService
import com.dicoding.storyapp.core.domain.repository.IStoryPagingRepository

class StoryPagingRepository private constructor(
    private val apiService: ApiService,
    private val storyDatabase: StoryDatabase
): IStoryPagingRepository {

    override fun getAllStories(token: String) : LiveData<PagingData<StoryEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, token),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStories()
            }
        ).liveData
    }

    companion object {
        @Volatile
        private var instance: StoryPagingRepository? = null

        fun getInstance(
            apiService: ApiService,
            storyDatabase: StoryDatabase
        ): StoryPagingRepository =
            instance ?: synchronized(this) {
                instance ?: StoryPagingRepository(apiService, storyDatabase)
            }.also { instance = it }
    }
}