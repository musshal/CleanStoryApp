package com.dicoding.storyapp.core.data.source.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.dicoding.storyapp.core.data.source.remote.network.ApiResponse
import com.dicoding.storyapp.core.data.source.remote.network.ApiService
import com.dicoding.storyapp.core.data.source.remote.request.LoginRequest
import com.dicoding.storyapp.core.data.source.remote.request.NewStoryRequest
import com.dicoding.storyapp.core.data.source.remote.request.RegisterRequest
import com.dicoding.storyapp.core.data.source.remote.response.AllStoriesResponse
import com.dicoding.storyapp.core.data.source.remote.response.DetailStoryResponse
import com.dicoding.storyapp.core.data.source.remote.response.LoginResponse
import com.dicoding.storyapp.core.data.source.remote.response.MessageResponse
import com.dicoding.storyapp.core.data.source.remote.response.StoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSource private constructor(private val apiService: ApiService){

    fun register(registerRequest: RegisterRequest): LiveData<ApiResponse<MessageResponse>> = liveData {
        emit(ApiResponse.Empty)
        try {
            val response = apiService.register(registerRequest)
            emit(ApiResponse.Success(response))
        } catch (exception: Exception) {
            Log.e("RemoteDataSource", "register: ${exception.message.toString()}")
            emit(ApiResponse.Error(exception.message.toString()))
        }
    }

    fun login(loginRequest: LoginRequest): LiveData<ApiResponse<LoginResponse>> = liveData {
        emit(ApiResponse.Empty)
        try {
            val response = apiService.login(loginRequest)
            emit(ApiResponse.Success(response))
        } catch (exception: Exception) {
            Log.e("RemoteDataSource", "login: ${exception.message.toString()}")
        }
    }

    fun addNewStory(
        token: String? = null,
        newStoryRequest: NewStoryRequest
    ): LiveData<ApiResponse<MessageResponse>> =
        liveData {
            emit(ApiResponse.Empty)
            try {
                if (token != null) {
                    if (token.isNotBlank()) {
                        val response = apiService.addNewStory(
                            "Bearer $token",
                            newStoryRequest.description,
                            newStoryRequest.photo
                        )
                        emit(ApiResponse.Success(response))
                    } else {
                        val response = apiService.addNewStory(
                            newStoryRequest.description,
                            newStoryRequest.photo
                        )
                        emit(ApiResponse.Success(response))
                    }
                }
            } catch (e: Exception) {
                Log.d("RemoteDataSource", "addNewStory: ${e.message.toString()}")
                emit(ApiResponse.Error(e.message.toString()))
            }
        }

    fun getAllStoriesWithLocation(token: String): LiveData<ApiResponse<List<StoryResponse>>> {
        val resultData = MutableLiveData<ApiResponse<List<StoryResponse>>>()
        val client = apiService.getAllStoriesWithLocation(token)

        client.enqueue(object : Callback<AllStoriesResponse> {
            override fun onResponse(
                call: Call<AllStoriesResponse>,
                response: Response<AllStoriesResponse>
            ) {
                val dataArray = response.body()?.listStory
                resultData.value =
                    if (dataArray != null) ApiResponse.Success(dataArray) else ApiResponse.Empty
            }

            override fun onFailure(call: Call<AllStoriesResponse>, t: Throwable) {
                resultData.value = ApiResponse.Error(t.message.toString())
                Log.e("RemoteDataSource", t.message.toString())
            }
        })

        return resultData
    }

    fun getDatailStory(token: String, id: String): LiveData<ApiResponse<StoryResponse>> {
        val resultData = MutableLiveData<ApiResponse<StoryResponse>>()
        val client = apiService.getDetailStory(token, id)

        client.enqueue(object : Callback<DetailStoryResponse> {
            override fun onResponse(
                call: Call<DetailStoryResponse>,
                response: Response<DetailStoryResponse>
            ) {
                val dataObject = response.body()?.story
                resultData.value =
                    if (dataObject != null) ApiResponse.Success(dataObject) else ApiResponse.Empty
            }

            override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                resultData.value = ApiResponse.Error(t.message.toString())
                Log.e("RemoteDataSource", t.message.toString())
            }
        })

        return resultData
    }

    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(apiService: ApiService): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource(apiService)
            }
    }
}