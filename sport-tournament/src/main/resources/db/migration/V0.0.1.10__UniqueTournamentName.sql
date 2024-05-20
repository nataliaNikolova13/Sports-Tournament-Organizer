ALTER TABLE tournaments
ADD CONSTRAINT UK_tournament_name UNIQUE (tournament_name);