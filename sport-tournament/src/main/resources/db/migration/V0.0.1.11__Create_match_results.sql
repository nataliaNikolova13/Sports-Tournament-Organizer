CREATE TABLE match_results (
    id BIGINT NOT NULL AUTO_INCREMENT,
    match_id BIGINT NOT NULL,
    winning_team_id BIGINT NOT NULL,
    score_team1 INT NOT NULL,
    score_team2 INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (match_id) REFERENCES matches(id),
    FOREIGN KEY (winning_team_id) REFERENCES teams(id)
);