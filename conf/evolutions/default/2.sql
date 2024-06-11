
# --- !Ups


INSERT INTO planes (id, model, capacity) VALUES (1, 'Boeing 747', 400);
INSERT INTO planes (id, model, capacity) VALUES (2, 'Airbus A380', 600);
INSERT INTO planes (id, model, capacity) VALUES (3, 'Boeing 737', 200);
INSERT INTO planes (id, model, capacity) VALUES (4, 'Airbus A320', 150);
INSERT INTO planes (id, model, capacity) VALUES (5, 'Boeing 777', 300);
INSERT INTO planes (id, model, capacity) VALUES (6, 'Airbus A330', 250);




INSERT INTO airports (id, name, code, city, country) VALUES (1, 'Los Angeles International Airport', 'LAX', 'Los Angeles', 'USA');
INSERT INTO airports (id, name, code, city, country) VALUES (2, 'JFK International Airport', 'JFK', 'New York', 'USA');
INSERT INTO airports (id, name, code, city, country) VALUES (3, 'London Heathrow', 'LHR', 'London', 'UK');
INSERT INTO airports (id, name, code, city, country) VALUES (4, 'Schiphol', 'AMS', 'Amsterdam', 'Netherlands');
INSERT INTO airports (id, name, code, city, country) VALUES (5, 'Charles de Gaulle', 'CDG', 'Paris', 'France');
INSERT INTO airports (id, name, code, city, country) VALUES (6, 'Frankfurt Airport', 'FRA', 'Frankfurt', 'Germany');
INSERT INTO airports (id, name, code, city, country) VALUES (7, 'Beijing Capital International Airport', 'PEK', 'Beijing', 'China');
INSERT INTO airports (id, name, code, city, country) VALUES (8, 'Dubai International Airport', 'DXB', 'Dubai', 'UAE');
INSERT INTO airports (id, name, code, city, country) VALUES (9, 'Tokyo Haneda Airport', 'HND', 'Tokyo', 'Japan');
INSERT INTO airports (id, name, code, city, country) VALUES (10, 'Singapore Changi Airport', 'SIN', 'Singapore', 'Singapore');


INSERT INTO flights (id, departure_airport_id, arrival_airport_id, departure_time, arrival_time, plane_id, status) VALUES (1, 1, 2, '2021-08-01 09:00:00', '2021-08-01 12:00:00', 1, 'Scheduled');
INSERT INTO flights (id, departure_airport_id, arrival_airport_id, departure_time, arrival_time, plane_id, status) VALUES (2, 2, 1, '2021-08-02 09:00:00', '2021-08-02 12:00:00', 2, 'Scheduled');
INSERT INTO flights (id, departure_airport_id, arrival_airport_id, departure_time, arrival_time, plane_id, status) VALUES (3, 3, 4, '2021-08-03 09:00:00', '2021-08-03 12:00:00', 3, 'Scheduled');
INSERT INTO flights (id, departure_airport_id, arrival_airport_id, departure_time, arrival_time, plane_id, status) VALUES (4, 4, 3, '2021-08-04 09:00:00', '2021-08-04 12:00:00', 1, 'Scheduled');
INSERT INTO flights (id, departure_airport_id, arrival_airport_id, departure_time, arrival_time, plane_id, status) VALUES (5, 5, 6, '2021-08-05 09:00:00', '2021-08-05 12:00:00', 2, 'Scheduled');
INSERT INTO flights (id, departure_airport_id, arrival_airport_id, departure_time, arrival_time, plane_id, status) VALUES (6, 6, 5, '2021-08-06 09:00:00', '2021-08-06 12:00:00', 3, 'Scheduled');
INSERT INTO flights (id, departure_airport_id, arrival_airport_id, departure_time, arrival_time, plane_id, status) VALUES (7, 7, 8, '2021-08-07 09:00:00', '2021-08-07 12:00:00', 1, 'Scheduled');
INSERT INTO flights (id, departure_airport_id, arrival_airport_id, departure_time, arrival_time, plane_id, status) VALUES (8, 8, 7, '2021-08-08 09:00:00', '2021-08-08 12:00:00', 6, 'Scheduled');
INSERT INTO flights (id, departure_airport_id, arrival_airport_id, departure_time, arrival_time, plane_id, status) VALUES (9, 9, 10, '2021-08-09 09:00:00', '2021-08-09 12:00:00', 5, 'Scheduled');
INSERT INTO flights (id, departure_airport_id, arrival_airport_id, departure_time, arrival_time, plane_id, status) VALUES (10, 10, 9, '2021-08-10 09:00:00', '2021-08-10 12:00:00', 4, 'Scheduled');
INSERT INTO flights (id, departure_airport_id, arrival_airport_id, departure_time, arrival_time, plane_id, status) VALUES (11, 1, 3, '2021-08-11 09:00:00', '2021-08-11 12:00:00', 1, 'Scheduled');
INSERT INTO flights (id, departure_airport_id, arrival_airport_id, departure_time, arrival_time, plane_id, status) VALUES (12, 3, 1, '2021-08-12 09:00:00', '2021-08-12 12:00:00', 2, 'Scheduled');
INSERT INTO flights (id, departure_airport_id, arrival_airport_id, departure_time, arrival_time, plane_id, status) VALUES (13, 4, 6, '2021-08-13 09:00:00', '2021-08-13 12:00:00', 3, 'Scheduled');
INSERT INTO flights (id, departure_airport_id, arrival_airport_id, departure_time, arrival_time, plane_id, status) VALUES (14, 6, 4, '2021-08-14 09:00:00', '2021-08-14 12:00:00', 1, 'Scheduled');



INSERT INTO users (id, name, email) VALUES (1, 'John Doe', 'john.doe@test.fr');
INSERT INTO users (id, name, email) VALUES (2, 'Jane Doe', 'jane.doe@test.fr');


insert into passwords (hash, password, email) values ('bcrypt', '$2a$12$qHm048CkXuTPYTTVF1AJaecIPlmTWA7befbykkr//nfJJcyPYIjxu', 'john.doe@test.fr');
insert into passwords (hash, password, email) values ('bcrypt', '$2a$12$qHm048CkXuTPYTTVF1AJaecIPlmTWA7befbykkr//nfJJcyPYIjxu', 'jane.doe@test.fr');
# --- !Downs