package com.example.nusalingo

import com.example.nusalingo.feature.auth.data.AuthValidator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AuthValidatorTest {
    @Test
    fun emailAcceptsValidAddressAndRejectsMalformedAddress() {
        assertNull(AuthValidator.email("nusa.lingo@example.co.id"))
        assertEquals("Format email tidak valid.", AuthValidator.email("nusa..lingo@example.com"))
        assertEquals("Format email tidak valid.", AuthValidator.email("nusa@example"))
    }

    @Test
    fun registrationRequiresStrongMatchingPassword() {
        assertNull(AuthValidator.registrationPassword("NusaLingo9!"))
        assertEquals(
            "Tambahkan minimal satu simbol.",
            AuthValidator.registrationPassword("NusaLingo9"),
        )
        assertNull(AuthValidator.registrationConfirmation("NusaLingo9!", "NusaLingo9!"))
        assertEquals(
            "Kata sandi tidak sama.",
            AuthValidator.registrationConfirmation("NusaLingo9!", "NusaLingo8!"),
        )
    }

    @Test
    fun registrationNameRejectsEmptyOrUnsafeCharacters() {
        assertNull(AuthValidator.registrationName("Nusa Lingo"))
        assertEquals("Nama tidak boleh kosong.", AuthValidator.registrationName("  "))
        assertEquals(
            "Nama hanya boleh berisi huruf, spasi, tanda hubung, atau apostrof.",
            AuthValidator.registrationName("Nusa<script>"),
        )
    }
}
