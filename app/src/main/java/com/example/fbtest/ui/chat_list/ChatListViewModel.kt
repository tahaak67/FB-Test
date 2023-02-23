package com.example.fbtest.ui.chat_list

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.fbtest.model.Conversation
import com.example.fbtest.util.Constants
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

/**
 * Created by Taha Ben Ashur (https://github.com/tahaak67) on 20,Feb,2023
 */
class ChatListViewModel : ViewModel() {

    private val database = Firebase.database
    private val auth = Firebase.auth
    private val convRef = database.getReference(Constants.CONVERSATIONS)
    var conversations = mutableStateOf(listOf<Conversation>())
        private set

    init {
        loadConversations()
    }

    fun loadConversations() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                dataSnapshot.getValue<List<Conversation>>()?.toList()?.let {
                    conversations.value = it
                }
                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("viewmodel", "loadPost:onCancelled", databaseError.toException())
            }
        }
        auth.currentUser?.uid?.let {uid ->
            convRef.child(uid).addValueEventListener(postListener)
        }
    }

}