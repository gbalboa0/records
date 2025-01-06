CREATE TABLE artist (
                        id BIGINT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        description TEXT
);

CREATE TABLE album (
                       id BIGINT PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       country VARCHAR(255),
                       year BIGINT,
                       artist_id BIGINT NOT NULL,
                       FOREIGN KEY (artist_id) REFERENCES artist(id) ON DELETE CASCADE
);

CREATE TABLE member (
                        id BIGINT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        is_active BOOLEAN,
                        artist_id BIGINT NOT NULL,
                        FOREIGN KEY (artist_id) REFERENCES artist(id) ON DELETE CASCADE
);

CREATE TABLE track (
                       id BIGINT PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       duration BIGINT,
                       position VARCHAR(50),
                       album_id BIGINT NOT NULL,
                       FOREIGN KEY (album_id) REFERENCES album(id) ON DELETE CASCADE
);

CREATE TABLE genre (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE album_genre (
                              album_id BIGINT NOT NULL,
                              genre_id BIGINT NOT NULL,
                              PRIMARY KEY (album_id, genre_id),
                              FOREIGN KEY (album_id) REFERENCES album(id) ON DELETE CASCADE,
                              FOREIGN KEY (genre_id) REFERENCES genre(id) ON DELETE CASCADE
);
