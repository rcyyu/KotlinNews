package com.kotlinnews.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.SparseArrayCompat
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
    private var BASE_URL: String = "https://www.reddit.com/"
    private var retrofit: Retrofit
    private var redditApi: RedditApi
    private var childrenList: List<Children> = ArrayList()
    private lateinit var recyclerView: RecyclerView
    private var adapter: ListAdapter = ListAdapter(childrenList)
    private var interceptor = HttpLoggingInterceptor()
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
        var manager = LinearLayoutManager(this)
        recyclerView.layoutManager = manager
        recyclerView.setHasFixedSize(true)
        loadFeed()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
    }

    private fun loadFeed() {
        val call = redditApi.getData()
        call.enqueue(object : Callback<Feed> {
            override fun onResponse(call: Call<Feed>, response: Response<Feed>) {
                val childrenList = response.body()!!.data.children
                adapter = ListAdapter(childrenList)
                recyclerView.adapter = adapter
            }

            override fun onFailure(call: Call<Feed>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }



}
