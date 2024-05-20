CREATE TABLE tournaments (
    id BIGINT NOT NULL AUTO_INCREMENT,
    tournament_name VARCHAR(255) NOT NULL,
    sport_type VARCHAR(255) NOT NULL,
    location_id BIGINT NOT NULL,
    start_at TIMESTAMP NOT NULL,
    end_at TIMESTAMP NOT NULL,
	PRIMARY KEY (id),
    FOREIGN KEY (location_id) REFERENCES locations(id) ON DELETE CASCADE,
    CONSTRAINT UK_tournament_name UNIQUE (tournament_name)
);