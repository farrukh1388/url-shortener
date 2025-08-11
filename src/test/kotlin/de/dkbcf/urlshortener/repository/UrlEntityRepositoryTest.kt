package de.dkbcf.urlshortener.repository

import de.dkbcf.urlshortener.entity.UrlEntity
import de.dkbcf.urlshortener.testcontainers.ContainerTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UrlEntityRepositoryTest(@Autowired val repository: UrlEntityRepository) : ContainerTest() {
    @Test
    fun `should successfully create and fetch new url entity`() {
        val entity = UrlEntity(shortCode = "1234able", originalUrl = "https://example.com")
        repository.save(entity)

        val found = repository.getUrlEntityByShortCode(entity.shortCode)

        assertThat(found.isPresent).isTrue()
        assertThat(found.get()).usingRecursiveComparison().isEqualTo(entity)
    }

    @Test
    fun `should return empty optional when url entity not found`() {
        val found = repository.getUrlEntityByShortCode("nonexistent")

        assertThat(found.isEmpty).isTrue()
    }
}