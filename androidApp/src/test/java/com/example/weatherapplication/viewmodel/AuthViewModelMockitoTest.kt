package com.example.weatherapplication.viewmodel

import com.example.weatherapplication.data.UserSessionManager
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class AuthViewModelMockitoTest {

    private lateinit var viewModel: AuthViewModel

    @Mock
    private lateinit var sessionManager: UserSessionManager

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        whenever(sessionManager.isLoggedIn()).thenReturn(false)
        viewModel = AuthViewModel(sessionManager)
    }

    @Test
    fun `login should update isLoggedIn state and call sessionManager`() {
        // Given
        val email = "test@example.com"

        // When
        viewModel.login(email)

        // Then
        assertEquals(true, viewModel.isLoggedIn.value)
        verify(sessionManager).setLoggedIn(email)
    }

    @Test
    fun `signup should update isLoggedIn state and call sessionManager`() {
        // Given
        val name = "Test User"
        val email = "test@example.com"

        // When
        viewModel.signup(name, email)

        // Then
        assertEquals(true, viewModel.isLoggedIn.value)
        verify(sessionManager).setLoggedIn(email)
    }

    @Test
    fun `logout should update isLoggedIn state and call sessionManager`() {
        // When
        viewModel.logout()

        // Then
        assertEquals(false, viewModel.isLoggedIn.value)
        verify(sessionManager).logout()
    }
}
