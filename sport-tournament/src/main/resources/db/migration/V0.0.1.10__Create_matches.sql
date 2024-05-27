CREATE TABLE matches (
    id BIGINT NOT NULL AUTO_INCREMENT,
    round_id BIGINT NOT NULL,
    team1_id BIGINT NOT NULL,
    team2_id BIGINT NOT NULL,
    match_time TIMESTAMP NOT NULL,
    venue_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (round_id) REFERENCES rounds(id),
    FOREIGN KEY (team1_id) REFERENCES teams(id),
    FOREIGN KEY (team2_id) REFERENCES teams(id),
    FOREIGN KEY (venue_id) REFERENCES venues(id)
);