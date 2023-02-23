package com.example.fbtest.ui.conversation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.fbtest.model.Contact
import com.example.fbtest.model.Message
import com.example.fbtest.util.Constants
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

/**
 * Created by Taha Ben Ashur (https://github.com/tahaak67) on 20,Feb,2023
 */
class ConversationsViewModel: ViewModel() {

    private val database = Firebase.database
    private val auth = Firebase.auth
    private val contactRef = database.getReference(Constants.CONTACTS)
    var contacts by mutableStateOf(listOf<Contact>())
        private set

    init {
        getContacts()
    }

    private fun getContacts() {
        contactRef.
        addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
               // println("data: ${dataSnapshot.children}")
                val contactsList = mutableListOf<Contact>()
                for (childSnapshot in dataSnapshot.children) {
                    val user1Email = childSnapshot.child("email").getValue(String::class.java).toString()
                    val uid = childSnapshot.child("uid").getValue(String::class.java).toString()

                    val conversation = Contact(user1Email, uid)
                    contactsList.add(conversation)
                }
                contacts = contactsList
                // Use the conversationList to display the list of conversations sent from the email
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("conversationsViewmodel", "loadConversations:onCancelled", databaseError.toException())
            }
        })
    }
}