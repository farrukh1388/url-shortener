package de.dkbcf.urlshortener.repository

import de.dkbcf.urlshortener.entity.UrlMapping
import de.dkbcf.urlshortener.testcontainers.ContainerTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UrlMappingRepositoryTest(@Autowired val repository: UrlMappingRepository) : ContainerTest() {
    @Test
    fun `should successfully create and fetch new url mapping`() {
        val entity = UrlMapping(shortCode = "1234able", originalUrl = "https://example.com")
        repository.save(entity)

        val found = repository.findById(entity.shortCode)

        assertThat(found.isPresent).isTrue()
        assertThat(found.get()).usingRecursiveComparison().isEqualTo(entity)
    }

    @Test
    fun `should return empty optional when url mapping not found`() {
        val found = repository.findById("nonexistent")

        assertThat(found.isEmpty).isTrue()
    }
}