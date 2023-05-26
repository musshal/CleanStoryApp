package com.dicoding.storyapp.core.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.dicoding.storyapp.core.data.NetworkBoundResource
import com.dicoding.storyapp.core.data.Resource
import com.dicoding.storyapp.core.data.source.local.LocalDataSource
import com.dicoding.storyapp.core.data.source.remote.RemoteDataSource
import com.dicoding.storyapp.core.data.source.remote.network.ApiResponse
import com.dicoding.storyapp.core.data.source.remote.request.NewStoryRequest
import com.dicoding.storyapp.core.data.source.remote.response.MessageResponse
import com.dicoding.storyapp.core.data.source.remote.response.StoryResponse
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

    override fun getAllStoriesWithLocation(token: String): LiveData<Resource<List<Story>>> =
        object : NetworkBoundResource<List<Story>, List<StoryResponse>>(appExecutors) {
            override fun loadFromDB(): LiveData<List<Story>> =
                Transformations.map(localDataSource.getAllStoriesWithLocation()) {
                    DataMapper.mapEntitiesToDomain(it)
                }

            override fun shouldFetch(data: List<Story>?): Boolean = true

            override fun createCall(): LiveData<ApiResponse<List<StoryResponse>>> =
                remoteDataSource.getAllStoriesWithLocation(token)

            override fun saveCallResult(data: List<StoryResponse>) {
                val storiesWithLocationList = DataMapper.mapResponsesToEntities(data)
                localDataSource.insertStory(storiesWithLocationList)
            }
        }.asLiveData()

//    override fun getDetailStory(token: String, id: String) : LiveData<Resource<Story>> =
//        object : NetworkBoundResource<Story, StoryResponse>(appExecutors) {
//
//
//            override fun shouldFetch(data: Story?): Boolean {}
//
//            override fun createCall(): LiveData<ApiResponse<StoryResponse>> {}
//
//            override fun saveCallResult(data: StoryResponse) {}
//        }.asLiveData()

    override fun setStoryBookmark(story: Story, bookmarkState: Boolean) {
        story.isBookmarked = bookmarkState
        val storyEntity = DataMapper.mapDomainToEntity(story)
        appExecutors.diskIO().execute { localDataSource.updateStory(storyEntity) }
    }

    override fun getBookmarkedStories() : LiveData<List<Story>> =
        Transformations.map(localDataSource.getBookmarkedStories()) {
            DataMapper.mapEntitiesToDomain(it)
        }

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