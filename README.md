# URL Shortener (Kotlin + Spring Boot)

## Improvements
 - originalUrl length can be increased

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