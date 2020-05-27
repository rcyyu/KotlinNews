package com.kotlinnews.api

import com.kotlinnews.model.Feed
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface RedditApi {
    @Headers("Content-Type: application/json", "User-Agent: android:com.example.kotlinnews:v1.0.0 (by /u/ghostofstuff)")
    @GET(".json")
    fun getData(): Call<Feed>
}
