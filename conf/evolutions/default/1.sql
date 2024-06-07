# --- !Ups


CREATE TABLE planes (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        model VARCHAR(255),
                        capacity INT
);

CREATE TABLE airports (
                          id INT PRIMARY KEY AUTO_INCREMENT,
                          name VARCHAR(255),
                          code VARCHAR(3),
                          city VARCHAR(255),
                          country VARCHAR(255)
);

CREATE TABLE flights (
                         id INT PRIMARY KEY  AUTO_INCREMENT,
                         departure_airport_id INT,
                         arrival_airport_id INT,
                         departure_time TIMESTAMP,
                         arrival_time TIMESTAMP,
                         plane_id INT,
                         status VARCHAR(255),
                         FOREIGN KEY (departure_airport_id) REFERENCES airports (id),
                         FOREIGN KEY (arrival_airport_id) REFERENCES airports (id),
                         FOREIGN KEY (plane_id) REFERENCES planes (id)
);

CREATE TABLE users (
                      id INT PRIMARY KEY AUTO_INCREMENT,
                      username VARCHAR(255),
                      password VARCHAR(255)
);

# --- !Downs
