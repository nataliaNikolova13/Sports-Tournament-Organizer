CREATE TABLE locations (
    id BIGINT NOT NULL AUTO_INCREMENT,
    location_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT UK_location_name UNIQUE (location_name)
);