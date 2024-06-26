ALTER TABLE users
ADD CONSTRAINT email_valid check(email like '%_@%_.%_');
