# URL Shortener (Kotlin + Spring Boot)

###### Minimal production-ready URL shortener (deterministic).

## Running locally on your machine
### Open a terminal (Bash), navigate to the project’s root folder, and run:
```bash
docker-compose -f docker-compose-setup-env.yml up -d
```
```bash
.\gradlew bootRun
```

## Running locally in a docker container
### Open a terminal (Bash), navigate to the project’s root folder, and run:
```bash
docker-compose -f docker-compose.yml up --build -d
```

##
## API Endpoints
### Shorten URL
`POST /api/v1/shorten`
```json
{
  "originalUrl": "https://example.com"
}
```
### Resolve short code and redirect
`GET /api/v1/{shortCode}`

##
## API Docs:
- http://localhost:8080/swagger-ui.html

##
## Assumptions and improvements
- originalUrl field length in entity can be increased to handle long urls
- To find bottlenecks or slow requests we can use Micrometer metric
- Short code generator could be upgraded by reducing the character set used only to letters and digits to avoid short codes like "ig-glNd-"
- Input field sanitizers can be added to increase security