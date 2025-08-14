package de.dkbcf.urlshortener.controller.api.v1

import de.dkbcf.urlshortener.dto.ShortenUrlRequest
import de.dkbcf.urlshortener.dto.ShortenUrlResponse
import de.dkbcf.urlshortener.service.UrlShortenerService
import jakarta.persistence.EntityNotFoundException
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest(UrlShortenerController::class)
class UrlShortenerControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired @MockitoBean val service: UrlShortenerService
) {
    @Test
    fun `should return 200 when url was shortened successfully`() {
        val originalUrl = "https://example.com/examples"
        val request = ShortenUrlRequest(originalUrl)
        val shortUrl = "https://bit.any/kW34ty7r"
        `when`(service.shortenUrl(request)).thenReturn(ShortenUrlResponse(shortUrl))

        mockMvc.post("/api/v1/shorten-url") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                        {
                            "originalUrl": "$originalUrl"
                        }
                      """
        }.andExpect {
            status { isOk() }
            content {
                jsonPath("$.shortUrl") { value(shortUrl) }
            }
        }
    }

    @ParameterizedTest(name = "should return 400 when request body like: {0}")
    @CsvSource(
        """{"originalUrl": " "}""",
        """{}""",
        """{"originalUrl": "invalid url"}""",
        """{"originalUrl": "files://domain.com"}""",
    )
    fun `should return 400 when request is invalid`(request: String) {
        mockMvc.post("/api/v1/shorten-url") {
            contentType = MediaType.APPLICATION_JSON
            content = request
        }.andExpect {
            status { isBadRequest() }
            content {
                jsonPath("$.detail") { value("Failed to read request") }
            }
        }
    }

    @Test
    fun `should return 302 when url was found by short code and redirected`() {
        val originalUrl = "https://example.com/examples"
        val shortCode = "kW34ty7r"
        `when`(service.resolve(shortCode)).thenReturn(originalUrl)

        mockMvc.get("/api/v1/$shortCode")
            .andExpect {
                status { isFound() }
                header { string("Location", originalUrl) }
            }
    }

    @Test
    fun `should return 404 when short code not found`() {
        val shortCode = "kW34ty7r"
        `when`(service.resolve(shortCode)).thenThrow(EntityNotFoundException("Short code not found"))

        mockMvc.get("/api/v1/$shortCode")
            .andExpect {
                status { isNotFound() }
                content {
                    jsonPath("$") { value("Short code not found") }
                }
            }
    }
}