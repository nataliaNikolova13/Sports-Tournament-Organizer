CREATE TABLE matches (
  id BIGINT NOT NULL AUTO_INCREMENT,
  tournament_id BIGINT NOT NULL,
  venue_id BIGINT NOT NULL,
  team_1 BIGINT NOT NULL,
  team_2 BIGINT NOT NULL,
  round BIGINT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  start_at TIMESTAMP NOT NULL,
  end_at TIMESTAMP NOT NULL,
  match_result ENUM('Team_1','Team_2','Draw','No_result') NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (tournament_id) REFERENCES tournaments(id),
  FOREIGN KEY (venue_id) REFERENCES venues(id),
  FOREIGN KEY (team_1) REFERENCES teams(id),
  FOREIGN KEY (team_2) REFERENCES teams(id),
  CONSTRAINT different_teams CHECK (team_1 != team_2)
);