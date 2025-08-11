package de.dkbcf.urlshortener.dto

import jakarta.validation.constraints.NotBlank
import java.net.MalformedURLException
import java.net.URI
import java.net.URISyntaxException

data class ShortenUrlRequest(
    @field:NotBlank
    val originalUrl: String
) {
    init {
        require(validateOriginalUrl(originalUrl)) { "Invalid URL format" }
    }
}

fun validateOriginalUrl(originalUrl: String): Boolean {
    try {
        URI(originalUrl).toURL()
        return true
    } catch (e: URISyntaxException) {
        return false
    } catch (e: MalformedURLException) {
        return false
    }
}