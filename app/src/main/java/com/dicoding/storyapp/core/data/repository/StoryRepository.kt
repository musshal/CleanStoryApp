package com.dicoding.storyapp.core.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.dicoding.storyapp.core.data.source.local.LocalDataSource
import com.dicoding.storyapp.core.data.source.local.entity.StoryEntity
import com.dicoding.storyapp.core.data.source.remote.RemoteDataSource
import com.dicoding.storyapp.core.data.source.remote.network.ApiResponse
import com.dicoding.storyapp.core.data.source.remote.request.NewStoryRequest
import com.dicoding.storyapp.core.data.source.remote.response.AllStoriesResponse
import com.dicoding.storyapp.core.data.source.remote.response.DetailStoryResponse
import com.dicoding.storyapp.core.data.source.remote.response.MessageResponse
import com.dicoding.storyapp.core.domain.model.Story
import com.dicoding.storyapp.core.domain.repository.IStoryRepository
import com.dicoding.storyapp.core.utils.AppExecutors
import com.dicoding.storyapp.core.utils.DataMapper

class StoryRepository private constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
    ): IStoryRepository {

    override fun addNewStory(
        token: String?,
        newStoryRequest: NewStoryRequest
    ): LiveData<ApiResponse<MessageResponse>> =
        remoteDataSource.addNewStory(token, newStoryRequest)

    override fun setStoryBookmark(story: Story, bookmarkState: Boolean) {
        val storyEntity = DataMapper.mapDomainToEntity(story)
        storyEntity.isBookmarked = bookmarkState
        appExecutors.diskIO().execute { localDataSource.updateStory(storyEntity) }
    }

    override fun getBookmarkedStories() : LiveData<List<Story>> =
        Transformations.map(localDataSource.getBookmarkedStories()) {
            DataMapper.mapEntitiesToDomain(it)
        }

    override fun getDetailStory(token: String, id: String) : LiveData<ApiResponse<DetailStoryResponse>> =
        remoteDataSource.getDetailStory(token, id)

    override fun getAllStoriesWithLocation(token: String): LiveData<ApiResponse<AllStoriesResponse>> =
        remoteDataSource.getAllStoriesWithLocation(token)

    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(
            remoteDataSource: RemoteDataSource,
            localDataSource: LocalDataSource,
            appExecutors: AppExecutors
        ) : StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(remoteDataSource, localDataSource, appExecutors)
            }.also { instance = it }
    }
}