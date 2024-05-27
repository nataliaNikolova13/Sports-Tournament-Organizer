CREATE TABLE rounds (
    id BIGINT NOT NULL AUTO_INCREMENT,
    tournament_id BIGINT NOT NULL,
    round_number INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (tournament_id) REFERENCES tournaments(id)
);