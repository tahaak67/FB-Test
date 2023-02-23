package com.example.fbtest.ui.conversation

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toFile
import androidx.lifecycle.ViewModel
import com.example.fbtest.model.Message
import com.example.fbtest.util.Constants
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.*

/**
 * Created by Taha Ben Ashur (https://github.com/tahaak67) on 20,Feb,2023
 */
class ChatViewModel : ViewModel() {

    private val database = FirebaseDatabase.getInstance()
    private val storage = Firebase.storage
    private val imagesRef = storage.getReference("images")
    private val auth = Firebase.auth
    private val msgRef = database.getReference(Constants.MESSAGES)

    var state by mutableStateOf(ChatScreenState())
        private set
    var messages by mutableStateOf(listOf<Message>())
        private set

    init {
        getMessages()
        auth.currentUser?.email?.let {
            state = state.copy(myEmail = it)
        }
    }

    private fun getMessages() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                Log.d("viewmodel", "datachange")
                val messageList = mutableListOf<Message>()
                dataSnapshot.children.forEach { message ->
                    val from = message.child("from").getValue(String::class.java).toString()
                    val to = message.child("to").getValue(String::class.java).toString()
                    val content = message.child("content").getValue(String::class.java).toString()
                    val imageUrl = message.child("imageUrl").getValue(String::class.java)
                    val conversation = Message(from, to, content,imageUrl)
                    messageList.add(conversation)
                }
                messages = messageList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("viewmodel", "loadPost:onCancelled", databaseError.toException())
            }
        }
        msgRef.addValueEventListener(postListener)
    }

    fun sendMessage(to: String = "", text: String, imageUrl: String? = null) {
        if (text.isNotBlank()) {
            Log.d("viewmodel", "msg recived $text")
            val newmsgRef = msgRef.push()
            val message = Message(state.myEmail, to, text, imageUrl)
            newmsgRef.setValue(message)
        }
    }

    fun uploadImage(uri: Uri, bytes: ByteArray) {
        println("uri: $uri")
        println("uri: ${uri}")
        println("path: ${uri.path}")
        val fileExtension =
            uri.path?.takeLastWhile { it != '.' }
        val fileName = UUID.randomUUID().toString() //+ "." + fileExtension
        println("uri: $uri")
        println("path: ${uri.path}")
        println("fileextension: ${fileExtension}")
        println("filename: ${fileName}")
        val newImageRef = imagesRef.child(fileName)
        newImageRef.putBytes(bytes).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("viewModel", "path: ${task.result.metadata?.path}")

                newImageRef.downloadUrl.addOnCompleteListener {
                    if (it.isSuccessful){
                        Log.d("viewModel", "url success ${it.result.toString()}")
                        sendMessage(text = "Picture", imageUrl = it.result.toString())
                    }else{
                        it.exception?.printStackTrace()
                        Log.d("viewModel", "url failed")
                    }
                }
                Log.d("viewModel", "upload success")
            } else {
                Log.d("viewModel", "upload failed")
                task.exception?.printStackTrace()
            }
        }
    }

}