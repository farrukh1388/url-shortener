package de.dkbcf.urlshortener.service

import de.dkbcf.urlshortener.dto.ShortenUrlRequest
import de.dkbcf.urlshortener.entity.UrlMapping
import de.dkbcf.urlshortener.properties.UrlShortenerProperties
import de.dkbcf.urlshortener.repository.UrlMappingRepository
import jakarta.persistence.EntityNotFoundException
import java.util.Optional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.any
import org.mockito.Mockito.anyString
import org.mockito.Mockito.argThat
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class UrlShortenerServiceTest {
    private val repository: UrlMappingRepository = mock(UrlMappingRepository::class.java)
    private val properties: UrlShortenerProperties = mock(UrlShortenerProperties::class.java)
    private val service = UrlShortenerService(repository = repository, properties = properties)

    @Test
    fun `should return shortened url and create new entity`() {
        val originalUrl = "https://example.com/examples"
        val shortCodeSize = 6
        val baseUrl = "https://bit.any/"
        val request = ShortenUrlRequest(originalUrl)

        `when`(properties.shortCodeSize).thenReturn(shortCodeSize)
        `when`(properties.baseUrl).thenReturn(baseUrl)
        `when`(repository.getUrlMappingByShortCode(anyString())).thenReturn(Optional.empty())

        val response = service.shortenUrl(request)

        assertThat(response).isNotNull
        assertThat(response.shortUrl).startsWith(baseUrl)
        assertThat(response.shortUrl.substringAfter(baseUrl)).hasSize(shortCodeSize)

        verify(
            repository,
            times(1)
        ).save(argThat { it.shortCode.length == shortCodeSize && it.originalUrl == originalUrl })
    }

    @Test
    fun `should return shortened url and do not create new entity when entity with the same short code already exists`() {
        val originalUrl = "https://example.com/examples"
        val shortCodeSize = 8
        val baseUrl = "https://bit.any/"
        val request = ShortenUrlRequest(originalUrl)

        `when`(properties.shortCodeSize).thenReturn(shortCodeSize)
        `when`(properties.baseUrl).thenReturn(baseUrl)
        `when`(repository.getUrlMappingByShortCode(anyString())).thenAnswer {
            Optional.of(
                UrlMapping(
                    shortCode = it.arguments[0].toString(),
                    originalUrl = originalUrl
                )
            )
        }

        val response = service.shortenUrl(request)

        assertThat(response).isNotNull
        assertThat(response.shortUrl).startsWith(baseUrl)
        assertThat(response.shortUrl.substringAfter(baseUrl)).hasSize(shortCodeSize)

        verify(
            repository,
            times(0)
        ).save(any(UrlMapping::class.java))
    }

    @Test
    fun `should return increased in size shortened url and create new entity`() {
        val originalUrl = "https://example.com/examples"
        val urlWithSimilarHash = "https://urlWithSimilarHash.com"
        val shortCodeSize = 6
        val baseUrl = "https://bit.any/"
        val request = ShortenUrlRequest(originalUrl)

        `when`(properties.shortCodeSize).thenReturn(shortCodeSize)
        `when`(properties.baseUrl).thenReturn(baseUrl)
        `when`(repository.getUrlMappingByShortCode(anyString())).thenAnswer {
            Optional.of(
                UrlMapping(
                    shortCode = it.arguments[0].toString(),
                    originalUrl = urlWithSimilarHash
                )
            )
        }.thenAnswer { Optional.empty<UrlMapping>() }

        val response = service.shortenUrl(request)

        assertThat(response).isNotNull
        assertThat(response.shortUrl).startsWith(baseUrl)
        assertThat(response.shortUrl.substringAfter(baseUrl)).hasSize(shortCodeSize + 1)

        verify(
            repository,
            times(1)
        ).save(argThat { it.shortCode.length == shortCodeSize + 1 && it.originalUrl == originalUrl })
        verify(repository, times(2)).getUrlMappingByShortCode(anyString())
    }

    @Test
    fun `should resolve short code to original url when mapping exists`() {
        val originalUrl = "https://example.com/examples"
        val shortCode = "kW34ty7r"

        `when`(repository.getUrlMappingByShortCode(shortCode)).thenReturn(
            Optional.of(
                UrlMapping(
                    shortCode = shortCode,
                    originalUrl = originalUrl
                )
            )
        )

        val response = service.resolve(shortCode)

        assertThat(response).isNotNull
        assertThat(response).isEqualTo(originalUrl)
    }

    @Test
    fun `should throw exception when mapping do not exists`() {
        val shortCode = "kW34ty7r"
        `when`(repository.getUrlMappingByShortCode(shortCode)).thenReturn(Optional.empty())

        val thrown = assertThrows<EntityNotFoundException> { service.resolve(shortCode) }

        assertThat(thrown).hasMessage("Short code not found")
    }
}