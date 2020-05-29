package com.kotlinnews.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kotlinnews.R
import com.kotlinnews.model.children.Children
import com.kotlinnews.activities.*

class ListAdapter(childrenList: List<Children>) : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {
    private var childrenList: List<Children> = childrenList
    private lateinit var context: Context
    private lateinit var child: Children

    override fun getItemCount(): Int {
        return childrenList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        context = parent.context
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val child = childrenList[position]
        holder.title.text = child.data.title
        GlideApp.with(context.applicationContext)
            .load(child.data.thumbnail)
            .fitCenter()
            .into(holder.thumbnail)
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var title: TextView
        var thumbnail: ImageView

        init {
            title = view.findViewById(R.id.title) as TextView
            thumbnail = view.findViewById(R.id.thumbnail) as ImageView
            thumbnail.setOnClickListener(this)
            view.setOnClickListener(this)
        }

        private fun getChildPosition() {
            val i = adapterPosition
            child = childrenList.get(i)
        }

        override fun onClick(view: View) {
            getChildPosition()
            val intent = Intent(context, DisplayItemActivity::class.java)
            intent.putExtra("title", child.data.title)
            intent.putExtra("selftext", child.data.selftext)
            intent.putExtra("thumbnail", child.data.thumbnail)
            intent.putExtra("url", child.data.url)
            context.startActivity(intent)
        }

    }
}