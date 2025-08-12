package de.dkbcf.urlshortener.repository

import de.dkbcf.urlshortener.entity.UrlEntity
import java.util.Optional
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository

interface UrlEntityRepository : JpaRepository<UrlEntity, UUID> {
    fun getUrlEntityByShortCode(shortCode: String): Optional<UrlEntity>
}