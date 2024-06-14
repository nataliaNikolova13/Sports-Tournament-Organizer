CREATE TABLE tournament_participants (
    id BIGINT NOT NULL AUTO_INCREMENT,
    team_id BIGINT NOT NULL,
    tournament_id BIGINT NOT NULL,
    status ENUM('joined', 'left','queued')  NOT NULL,
    time_stamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE CASCADE,
    FOREIGN KEY (tournament_id) REFERENCES tournaments(id) ON DELETE CASCADE
);