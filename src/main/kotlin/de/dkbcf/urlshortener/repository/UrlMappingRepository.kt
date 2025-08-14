package de.dkbcf.urlshortener.repository

import de.dkbcf.urlshortener.entity.UrlMapping
import java.util.Optional
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository

interface UrlMappingRepository : JpaRepository<UrlMapping, UUID> {
    fun getUrlMappingByShortCode(shortCode: String): Optional<UrlMapping>
}