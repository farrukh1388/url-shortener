package de.dkbcf.urlshortener.testcontainers

import org.testcontainers.containers.PostgreSQLContainer

private const val IMAGE_VERSION = "postgres:17.5"
private const val DB_NAME = "urlshortenerdb"
private const val DB_USER = "postgres"
private const val DB_PASSWORD = "pass"

class PostgresTestContainer(image: String) : PostgreSQLContainer<PostgresTestContainer>(image) {
    override fun start() {
        super.start()
        System.setProperty("spring.datasource.url", instance.jdbcUrl)
        System.setProperty("spring.datasource.username", instance.username)
        System.setProperty("spring.datasource.password", instance.password)
        System.setProperty("spring.jpa.hibernate.ddl-auto", "update")
    }

    override fun stop() {
    }

    companion object {
        val instance = PostgresTestContainer(IMAGE_VERSION).apply {
            withDatabaseName(DB_NAME)
            withUsername(DB_USER)
            withPassword(DB_PASSWORD)
        }
    }
}