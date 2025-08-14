package de.dkbcf.urlshortener.controller.api.v1

import de.dkbcf.urlshortener.dto.ShortenUrlRequest
import de.dkbcf.urlshortener.dto.ShortenUrlResponse
import de.dkbcf.urlshortener.service.UrlShortenerService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class UrlShortenerController(private val service: UrlShortenerService) {

    @Operation(summary = "Create a short code for URL")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Short code created",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ShortenUrlResponse::class)
                )]
            ),
            ApiResponse(responseCode = "400", description = "Invalid URL")
        ]
    )
    @PostMapping("/shorten-url")
    fun shortenUrl(@RequestBody request: ShortenUrlRequest): ShortenUrlResponse {
        return service.shortenUrl(request)
    }

    @Operation(summary = "Resolve short code to original URL and redirect")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "302", description = "Redirect to original URL"),
            ApiResponse(responseCode = "404", description = "Short code not found")
        ]
    )
    @GetMapping("/{shortCode}")
    fun resolve(@PathVariable shortCode: String): ResponseEntity<Any> {
        val originalUrl = service.resolve(shortCode)
        return ResponseEntity.status(302).header("Location", originalUrl).build()
    }
}