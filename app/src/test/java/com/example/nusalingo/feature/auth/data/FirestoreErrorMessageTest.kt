package com.example.nusalingo.feature.auth.data

import org.junit.Assert.assertTrue
import org.junit.Test

class FirestoreErrorMessageTest {
    @Test
    fun permissionDeniedExplainsHowToFixFirestoreRules() {
        val message = firestoreErrorMessage("PERMISSION_DENIED")

        assertTrue(message.contains("firestore.rules"))
        assertTrue(message.contains("Firebase Console"))
    }

    @Test
    fun failedPreconditionExplainsFirestoreSetup() {
        val message = firestoreErrorMessage("FAILED_PRECONDITION")

        assertTrue(message.contains("Cloud Firestore"))
        assertTrue(message.contains("aktifkan"))
    }
}
