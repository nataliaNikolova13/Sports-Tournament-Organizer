CREATE TABLE venues (
    id BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    PRIMARY KEY (id, location_id),
    FOREIGN KEY (location_id) REFERENCES locations(id)
);