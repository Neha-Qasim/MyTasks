package com.neha.mytasks.auth

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.*
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

data class UserProfile(
    val name: String = "",
    val email: String = "",
    val lastLogin: String = ""
)

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().reference

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isLoggedIn = MutableStateFlow(auth.currentUser != null)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    init {
        if (currentUser != null) {
            fetchUserProfile()
        }

        auth.addAuthStateListener {
            _isLoggedIn.value = it.currentUser != null
            if (it.currentUser != null) {
                fetchUserProfile()
            }
        }
    }

    /**
     * Sign up new user with validation
     */
    fun signUpWithEmail(
        name: String,
        email: String,
        password: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            // Email validation
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                _error.value = "Invalid email. Example: example@email.com"
                _isLoading.value = false
                return@launch
            }

            // Password validation
            if (password.length < 6) {
                _error.value = "Password must be at least 6 characters"
                _isLoading.value = false
                return@launch
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    _isLoading.value = false
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        val uid = user?.uid ?: return@addOnCompleteListener

                        val profile = UserProfile(
                            name = name,
                            email = email,
                            lastLogin = Date().toString()
                        )

                        db.child("users").child(uid).child("profile")
                            .setValue(profile)
                            .addOnSuccessListener {
                                _userProfile.value = profile
                                onResult(true)
                            }
                            .addOnFailureListener {
                                _error.value = "Failed to save profile"
                                onResult(false)
                            }

                    } else {
                        _error.value = getFirebaseSignUpError(task.exception)
                        onResult(false)
                    }
                }
        }
    }

    /**
     * Sign in existing user with validation
     */
    fun signInWithEmail(email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            // Email validation
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                _error.value = "Invalid email. Example: example@email.com"
                _isLoading.value = false
                return@launch
            }

            // Password validation
            if (password.length < 6) {
                _error.value = "Password must be at least 6 characters"
                _isLoading.value = false
                return@launch
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    _isLoading.value = false
                    if (task.isSuccessful) {
                        fetchUserProfile()
                        onResult(true)
                    } else {
                        _error.value = getFirebaseSignInError(task.exception)
                        onResult(false)
                    }
                }
        }
    }

    /**
     * Provide user-friendly Firebase sign-up error messages
     */
    private fun getFirebaseSignUpError(exception: Exception?): String {
        return when (exception) {
            is FirebaseAuthWeakPasswordException -> "Password is too weak. Minimum 6 characters required."
            is FirebaseAuthInvalidCredentialsException -> "Invalid email format."
            is FirebaseAuthUserCollisionException -> "Email is already in use."
            else -> exception?.localizedMessage ?: "Sign-up failed. Please try again."
        }
    }

    /**
     * Provide user-friendly Firebase sign-in error messages
     */
    private fun getFirebaseSignInError(exception: Exception?): String {
        return when (exception) {
            is FirebaseAuthInvalidCredentialsException -> "Invalid credentials. Check email or password."
            is FirebaseAuthInvalidUserException -> "No account found with this email."
            else -> exception?.localizedMessage ?: "Sign-in failed. Please try again."
        }
    }

    /**
     * Fetch user profile from database
     */
    private fun fetchUserProfile() {
        val uid = currentUser?.uid ?: return

        db.child("users").child(uid).child("profile")
            .get()
            .addOnSuccessListener { snapshot ->
                snapshot.getValue(UserProfile::class.java)?.let {
                    _userProfile.value = it
                }
            }
            .addOnFailureListener {
                _error.value = "Failed to load profile"
            }
    }

    /**
     * Sign out the user and reset state
     */
    fun signOut() {
        auth.signOut()
        _isLoggedIn.value = false
        _userProfile.value = null
    }

    /**
     * Clear error message manually
     */
    fun clearError() {
        _error.value = null
    }

    /**
     * Optional: Set a custom error manually
     */
    fun setError(message: String) {
        _error.value = message
     }
}
