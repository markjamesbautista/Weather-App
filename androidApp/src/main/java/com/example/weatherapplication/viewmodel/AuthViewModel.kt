package com.example.weatherapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.weatherapplication.data.UserSessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val sessionManager: UserSessionManager
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(sessionManager.isLoggedIn())
    val isLoggedIn = _isLoggedIn.asStateFlow()

    fun login(email: String) {
        sessionManager.setLoggedIn(email)
        _isLoggedIn.value = true
    }

    fun signup(name: String, email: String) {
        // Simplified signup logic
        sessionManager.setLoggedIn(email)
        _isLoggedIn.value = true
    }

    fun logout() {
        sessionManager.logout()
        _isLoggedIn.value = false
    }
}
