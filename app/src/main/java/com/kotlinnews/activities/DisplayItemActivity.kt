package com.kotlinnews.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.kotlinnews.R

class DisplayItemActivity : AppCompatActivity() {
    private lateinit var thumbnailImageView: ImageView
    private lateinit var bodyTextView: TextView
    private lateinit var urlTextView: TextView
    private lateinit var title: String
    private lateinit var selftext: String
    private lateinit var thumbnail: String
    private lateinit var url: String
    private lateinit var extras: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_item)

        thumbnailImageView = findViewById(R.id.itemthumbnail)
        bodyTextView = findViewById(R.id.itembody)
        urlTextView = findViewById(R.id.itemurl)
        getBundleExtras()
        setTitle(title)
        setTextView()
        loadImageThumbnail()

        urlTextView.setOnClickListener { view ->
            if (url !== "") {
                val intent = Intent(view.context, WebviewActivity::class.java)
                intent.putExtra("url", url)
                startActivity(intent)
            }
        }
    }

    private fun loadImageThumbnail() {
        Glide.with(this)
            .load(thumbnail)
            .into(thumbnailImageView)
    }

    private fun getBundleExtras() {
        extras = intent.extras!!
        title = extras.getString("title").toString()
        selftext = extras.getString("selftext").toString()
        thumbnail = extras.getString("thumbnail").toString()
        url = extras.getString("url").toString()
    }

    private fun setTextView() {
        bodyTextView.text = selftext
    }
}
