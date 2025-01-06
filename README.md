# Run using Docker compose
This application can be ran using docker compose. The docker compose file will start a postgres database and the application.
## Build the project
```shell
mvn clean package
```

## Build and run the docker compose
```shell
docker-compose up --build
```

# Swagger

You can access the swagger documentation at:

    http://localhost:8080/swagger-ui.html

Endpoints are public just for the sake of this test. In a real world scenario, they should be secured
in the SecurityConfig.