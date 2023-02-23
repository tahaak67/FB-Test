package com.example.fbtest.ui.login_signup

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.fbtest.model.Contact
import com.example.fbtest.util.Constants
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

/**
 * Created by Taha Ben Ashur (https://github.com/tahaak67) on 19,Feb,2023
 */
class AuthenticationViewModel : ViewModel() {

    private val database = Firebase.database
    private val auth = Firebase.auth
    private val contactRef = database.getReference(Constants.CONTACTS)//.getReference(Constants.CONTACTS)
    var isLoggedIn by mutableStateOf(false)
        private set
    var isLoading by mutableStateOf(false)
        private set

    init {
        isUserLoggedIn()
    }

    fun isUserLoggedIn(): Boolean {
        isLoggedIn = try {
            Log.d("viewModel", "user: ${auth.currentUser}")
            auth.currentUser != null
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
        return isLoggedIn
    }

    fun register(email: String, password: String) {
        try {
            isLoading = true
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("viewModel", "signup Successful")
                    auth.currentUser?.let { user ->
                        val newContactRef = contactRef.push()
                        val newContact = Contact(user.email!!,user.uid)
                        newContactRef.setValue(newContact).addOnFailureListener {
                            it.printStackTrace()
                            Log.d("viewModel", "write Failed! ")

                        }
                    }
                    isLoggedIn = true
                } else {
                    Log.d("viewModel", "signup Failed!")
                }
            }
        } catch (e: Exception) {
            Log.e("viewModel", "")
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    fun signIn(email: String, password: String) {
        try {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("viewModel", "signin Successful")
                    isLoggedIn = true
                } else {
                    Log.d("viewModel", "signin Failed!")
                }

            }
        } catch (e: Exception) {
            Log.e("viewModel", "")
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

}