package com.dicoding.storyapp.core.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import com.dicoding.storyapp.core.data.source.local.entity.StoryEntity

@Dao
interface StoryDao {
    @Query("SELECT * FROM stories WHERE isBookmarked == 0 ORDER BY createdAt DESC")
    fun getAllStories(): PagingSource<Int, StoryEntity>

    @Query("SELECT * FROM stories WHERE lat != NULL AND lon != NULL")
    fun getAllStoriesWithLocation(): LiveData<List<StoryEntity>>

    @Query("SELECT * FROM stories WHERE id = :id")
    fun getDetailStory(id: String): LiveData<StoryEntity>

    @Query("SELECT * FROM stories where isBookmarked = 1")
    fun getBookmarkedStories(): LiveData<List<StoryEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertStory(stories: List<StoryEntity>)

    @Update
    fun updateStory(story: StoryEntity)

    @Query("DELETE FROM stories WHERE isBookmarked = 0")
    fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM stories WHERE id = :id AND isBookmarked = 1)")
    fun isStoryBookmarked(id: String): Boolean
}