CREATE TABLE venues (
    id BIGINT NOT NULL AUTO_INCREMENT,
    number BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (location_id) REFERENCES locations(id) ON DELETE CASCADE
);