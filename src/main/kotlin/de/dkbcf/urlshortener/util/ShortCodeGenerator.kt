package de.dkbcf.urlshortener.util

import java.security.MessageDigest
import java.util.Base64

object ShortCodeGenerator {
    fun generate(url: String, size: Int): String {
        val digest = MessageDigest.getInstance("SHA-256").digest(url.toByteArray())
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest).take(size)
    }
}