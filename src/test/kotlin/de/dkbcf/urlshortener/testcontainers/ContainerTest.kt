package de.dkbcf.urlshortener.testcontainers

import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
open class ContainerTest {
    companion object {
        @Container
        @Suppress("unused")
        val postgresTestContainer = PostgresTestContainer.instance
    }
}