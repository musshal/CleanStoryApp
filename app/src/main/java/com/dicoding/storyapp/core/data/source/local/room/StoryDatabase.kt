package com.dicoding.storyapp.core.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.storyapp.core.data.source.local.entity.RemoteKeys
import com.dicoding.storyapp.core.data.source.local.entity.StoryEntity

@Database(entities = [StoryEntity::class, RemoteKeys::class], version = 1, exportSchema = false)
abstract class StoryDatabase : RoomDatabase() {

    abstract fun storyDao(): StoryDao

    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: StoryDatabase? = null
        fun getInstance(context: Context): StoryDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                StoryDatabase::class.java, "stories_db"
            ).fallbackToDestructiveMigration().build()
        }
    }
}