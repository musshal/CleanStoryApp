package com.dicoding.storyapp.core.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import androidx.paging.map
import com.dicoding.storyapp.core.data.paging.StoryRemoteMediator
import com.dicoding.storyapp.core.data.source.local.room.StoryDatabase
import com.dicoding.storyapp.core.data.source.remote.network.ApiService
import com.dicoding.storyapp.core.domain.model.Story
import com.dicoding.storyapp.core.domain.repository.IStoryPagingRepository

class StoryPagingRepository private constructor(
    private val apiService: ApiService,
    private val storyDatabase: StoryDatabase
): IStoryPagingRepository {

    override fun getAllStories(token: String) : LiveData<PagingData<Story>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, token),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStories()
            }
        ).liveData.map { pagingData ->
            pagingData.map { storyEntity ->
                Story(
                    id = storyEntity.id,
                    name = storyEntity.name,
                    description = storyEntity.description,
                    photoUrl = storyEntity.photoUrl,
                    createdAt = storyEntity.createdAt,
                    lat = storyEntity.lat,
                    lon = storyEntity.lon,
                    isBookmarked = storyEntity.isBookmarked
                )
            }
        }
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