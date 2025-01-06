FROM postgres:latest

# Use model.sql as the initialization script
COPY src/main/resources/sql/model.sql /docker-entrypoint-initdb.d/
