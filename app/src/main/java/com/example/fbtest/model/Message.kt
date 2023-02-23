package com.example.fbtest.model

/**
 * Created by Taha Ben Ashur (https://github.com/tahaak67) on 20,Feb,2023
 */
data class Message(
    val from: String,
    val to: String,
    val content: String,
    val imageUrl: String? = null
)
