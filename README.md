# Run DB with docker

Build the docker image:

    docker build -t postgres-db .

Run the docker image:

    docker run --name postgres-db \
    -e POSTGRES_USER=clara \
    -e POSTGRES_PASSWORD=clara123 \
    -e POSTGRES_DB=discography \
    -p 5432:5432 \
    -d postgres-db

# Run the app

You can run it using intellij or using the command line:

    mvn spring-boot:run

# Swagger

You can access the swagger documentation at:

    http://localhost:8080/swagger-ui.html

Endpoints are public just for the sake of this test. In a real world scenario, they should be secured
in the SecurityConfig.