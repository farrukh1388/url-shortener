package de.dkbcf.urlshortener.dto

import java.net.MalformedURLException
import java.net.URI
import java.net.URISyntaxException

data class ShortenUrlRequest(
    val originalUrl: String? = null
) {
    init {
        require(validateOriginalUrl(originalUrl)) { "Invalid URL format" }
    }
}

fun validateOriginalUrl(originalUrl: String?): Boolean {
    if (originalUrl.isNullOrBlank()) {
        return false
    }
    try {
        URI(originalUrl).toURL()
        return true
    } catch (e: URISyntaxException) {
        return false
    } catch (e: MalformedURLException) {
        return false
    }
}