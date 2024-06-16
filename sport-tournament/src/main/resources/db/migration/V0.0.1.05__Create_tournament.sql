CREATE TABLE tournaments (
    id BIGINT NOT NULL AUTO_INCREMENT,
    tournament_name VARCHAR(255) NOT NULL,
    sport_type VARCHAR(255) NOT NULL,
    tournament_category ENUM('amateur', 'professional', 'youth') NOT NULL,
    location_id BIGINT NOT NULL,
    start_at TIMESTAMP NOT NULL,
    end_at TIMESTAMP NOT NULL,
    start_hour INT NOT NULL,
    end_hour INT NOT NULL,
    team_count INT NOT NULL,
    match_duration INT NOT NULL,
    team_members_count INT NOT NULL,
	PRIMARY KEY (id),
    FOREIGN KEY (location_id) REFERENCES locations(id) ON DELETE CASCADE,
    CONSTRAINT UK_tournament_name UNIQUE (tournament_name)
);