package de.dkbcf.urlshortener.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.Objects
import org.hibernate.annotations.UuidGenerator

@Entity
@Table(name = "urls", indexes = [Index(name = "idx_short_code", columnList = "short_code", unique = true)])
data class UrlEntity(
    @Id
    @UuidGenerator
    @Column(length = 36)
    val id: String? = null,

    @Column(nullable = false, name = "short_code", length = 10, unique = true)
    val shortCode: String,

    @Column(nullable = false, name = "original_url", length = 2048)
    val originalUrl: String,

    @Column(nullable = false, name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val otherUrlEntity: UrlEntity = other as UrlEntity
        return otherUrlEntity.shortCode == shortCode && otherUrlEntity.createdAt == createdAt
    }

    override fun hashCode(): Int {
        return Objects.hash(shortCode, createdAt)
    }
}
