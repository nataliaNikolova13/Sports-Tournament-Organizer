ALTER TABLE teams
ADD COLUMN tournament_id BIGINT,
ADD CONSTRAINT fk_tournament_id FOREIGN KEY (tournament_id) REFERENCES tournaments(id);