package com.kotlinnews.model

import com.kotlinnews.model.children.Children

data class Data (
    val modhash: String,
    val children: ArrayList<Children>,
    val after: String
)
