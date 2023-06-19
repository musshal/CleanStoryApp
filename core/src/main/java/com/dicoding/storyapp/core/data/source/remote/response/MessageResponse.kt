package com.dicoding.storyapp.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)