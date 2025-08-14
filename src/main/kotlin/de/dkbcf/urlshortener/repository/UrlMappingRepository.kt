package de.dkbcf.urlshortener.repository

import de.dkbcf.urlshortener.entity.UrlMapping
import org.springframework.data.jpa.repository.JpaRepository

interface UrlMappingRepository : JpaRepository<UrlMapping, String>