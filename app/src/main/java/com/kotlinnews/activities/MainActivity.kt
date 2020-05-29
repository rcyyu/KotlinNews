package com.kotlinnews.activities

import android.media.session.MediaSession
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kotlinnews.R
import com.kotlinnews.adapter.ListAdapter
import com.kotlinnews.api.RedditApi
import com.kotlinnews.model.Feed
import com.kotlinnews.model.children.Children
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private var BASE_URL: String = "https://www.reddit.com/r/kotlin/"
    private var retrofit: Retrofit
    private var redditApi: RedditApi
    private var childrenList: MutableList<Children> = ArrayList()
    private lateinit var after: String
    private lateinit var recyclerView: RecyclerView
    private var adapter: ListAdapter = ListAdapter(childrenList)
    private var interceptor = HttpLoggingInterceptor()
    private lateinit var recyclerViewState: Parcelable
    private var client: OkHttpClient

    init {
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        redditApi = retrofit.create(RedditApi::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.setHasFixedSize(true)
        loadFeed()
        setupRecyclerView()
        recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()!!

        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    Toast.makeText(this@MainActivity, "Loading Posts", Toast.LENGTH_LONG).show()
                    recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()!!
                    loadFeed(after)
                }
            }
        })
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
    }

    private fun loadFeed(afterPost: String = "") {
        val call = redditApi.getData(afterPost)
        call.enqueue(object : Callback<Feed> {
            override fun onResponse(call: Call<Feed>, response: Response<Feed>) {
                val responseBody = response.body()!!
                childrenList.addAll(responseBody.data.children)
                after = responseBody.data.after
                adapter = ListAdapter(childrenList)
                recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
                adapter.notifyDataSetChanged()
                recyclerView.adapter = adapter
            }

            override fun onFailure(call: Call<Feed>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
