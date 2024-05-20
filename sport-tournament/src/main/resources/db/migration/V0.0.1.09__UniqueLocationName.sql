ALTER TABLE locations
ADD CONSTRAINT UK_location_name UNIQUE (location_name);