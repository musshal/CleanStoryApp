package com.dicoding.storyapp.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.core.data.source.local.datastore.SettingPreferences
import com.dicoding.storyapp.core.data.source.local.datastore.UserPreferences
import com.dicoding.storyapp.core.data.source.local.entity.UserEntity
import kotlinx.coroutines.launch

class SettingViewModel(
    private val userPreferences: UserPreferences,
    private val settingPreferences: SettingPreferences
    ) : ViewModel() {

    fun saveThemeSetting(isDarkModeActive: Boolean) { viewModelScope.launch {
            settingPreferences.saveThemeSetting(isDarkModeActive)
        }
    }

    fun getThemeSetting() : LiveData<Boolean> = settingPreferences.getThemeSetting().asLiveData()

    fun getLogin() : LiveData<UserEntity> = userPreferences.getLogin().asLiveData()

    fun deleteLogin() { viewModelScope.launch { userPreferences.deleteLogin() } }
}