package com.example.nusalingo.feature.auth.data

import org.junit.Assert.assertEquals
import org.junit.Test

class FirebaseAuthErrorMessageTest {
    @Test
    fun networkExceptionExplainsThatConnectivityMustBeChecked() {
        assertEquals(
            "Koneksi internet bermasalah. Periksa jaringan lalu coba lagi.",
            firebaseAuthErrorMessage(errorCode = null, isNetworkError = true),
        )
    }

    @Test
    fun networkAuthErrorCodeExplainsThatConnectivityMustBeChecked() {
        assertEquals(
            "Koneksi internet bermasalah. Periksa jaringan lalu coba lagi.",
            firebaseAuthErrorMessage(
                errorCode = "ERROR_NETWORK_REQUEST_FAILED",
                isNetworkError = false,
            ),
        )
    }
}
