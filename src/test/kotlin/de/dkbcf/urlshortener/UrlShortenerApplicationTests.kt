package de.dkbcf.urlshortener

import de.dkbcf.urlshortener.testcontainers.ContainerTest
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UrlShortenerApplicationTests : ContainerTest() {

    @Test
    fun contextLoads() {
    }

}
