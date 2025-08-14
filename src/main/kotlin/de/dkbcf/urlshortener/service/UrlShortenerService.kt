package de.dkbcf.urlshortener.service

import de.dkbcf.urlshortener.dto.ShortenUrlRequest
import de.dkbcf.urlshortener.dto.ShortenUrlResponse
import de.dkbcf.urlshortener.entity.UrlEntity
import de.dkbcf.urlshortener.properties.UrlShortenerProperties
import de.dkbcf.urlshortener.repository.UrlEntityRepository
import de.dkbcf.urlshortener.util.ShortCodeGenerator
import jakarta.persistence.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
@EnableConfigurationProperties(UrlShortenerProperties::class)
class UrlShortenerService(
    private val repository: UrlEntityRepository,
    private val properties: UrlShortenerProperties
) {
    private val log = LoggerFactory.getLogger(UrlShortenerService::class.java)

    fun shortenUrl(request: ShortenUrlRequest): ShortenUrlResponse {
        val originalUrl = request.originalUrl!!
        log.debug("Shortening URL: {}", originalUrl)
        var shortCodeSize = properties.shortCodeSize
        var shortCode: String
        do {
            shortCode = ShortCodeGenerator.generate(originalUrl, shortCodeSize)
            val existingOptional = repository.getUrlEntityByShortCode(shortCode)
            if (existingOptional.isEmpty) {
                log.debug("Creating mapping {} -> {}", shortCode, originalUrl)
                repository.save(UrlEntity(shortCode = shortCode, originalUrl = originalUrl))
                break
            } else {
                val existing = existingOptional.get()
                if (existing.originalUrl == originalUrl) {
                    break
                } else {
                    shortCodeSize++
                    continue
                }
            }
        } while (true)

        return ShortenUrlResponse(properties.baseUrl + shortCode)
    }

    @Cacheable("urls")
    fun resolve(shortCode: String): String {
        log.debug("Resolving short code: {} to original URL", shortCode)
        return repository.getUrlEntityByShortCode(shortCode)
            .orElseThrow { EntityNotFoundException("Short code not found") }.originalUrl
    }
}