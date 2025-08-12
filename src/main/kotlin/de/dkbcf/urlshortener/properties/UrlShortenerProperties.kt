package de.dkbcf.urlshortener.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "url-shortener")
data class UrlShortenerProperties(val baseUrl: String, val shortCodeSize: Int)