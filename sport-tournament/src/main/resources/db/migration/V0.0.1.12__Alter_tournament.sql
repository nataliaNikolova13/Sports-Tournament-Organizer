ALTER TABLE tournaments ADD COLUMN start_hour INT NOT NULL;
ALTER TABLE tournaments ADD COLUMN end_hour INT NOT NULL;
ALTER TABLE tournaments ADD COLUMN team_count INT NOT NULL;
ALTER TABLE tournaments ADD COLUMN match_duration INT NOT NULL;