CREATE TABLE teams (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    category ENUM('amateur', 'professional', 'youth') NOT NULL,
    PRIMARY KEY (id)
);
