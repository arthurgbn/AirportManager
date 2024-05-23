INSERT INTO planes (id, model, capacity) VALUES (1, 'Boeing 747', 400);
INSERT INTO planes (id, model, capacity) VALUES (2, 'Airbus A380', 600);
INSERT INTO planes (id, model, capacity) VALUES (3, 'Boeing 737', 200);

INSERT INTO airports (id, name, code, city, country) VALUES (1, 'Los Angeles International Airport', 'LAX', 'Los Angeles', 'USA');
INSERT INTO airports (id, name, code, city, country) VALUES (2, 'JFK International Airport', 'JFK', 'New York', 'USA');
INSERT INTO airports (id, name, code, city, country) VALUES (3, 'London Heathrow', 'LHR', 'London', 'UK');
INSERT INTO airports (id, name, code, city, country) VALUES (4, 'Schiphol', 'AMS', 'Amsterdam', 'Netherlands');
INSERT INTO airports (id, name, code, city, country) VALUES (5, 'Charles de Gaulle', 'CDG', 'Paris', 'France');
INSERT INTO airports (id, name, code, city, country) VALUES (6, 'Frankfurt Airport', 'FRA', 'Frankfurt', 'Germany');

INSERT INTO flights (id, departure_airport_id, arrival_airport_id, departure_time, arrival_time, plane_id, status) VALUES (1, 1, 2, '2021-08-01 09:00:00', '2021-08-01 12:00:00', 1, 'Scheduled');
INSERT INTO flights (id, departure_airport_id, arrival_airport_id, departure_time, arrival_time, plane_id, status) VALUES (2, 2, 1, '2021-08-02 09:00:00', '2021-08-02 12:00:00', 2, 'Scheduled');
INSERT INTO flights (id, departure_airport_id, arrival_airport_id, departure_time, arrival_time, plane_id, status) VALUES (3, 3, 4, '2021-08-03 09:00:00', '2021-08-03 12:00:00', 3, 'Scheduled');
INSERT INTO flights (id, departure_airport_id, arrival_airport_id, departure_time, arrival_time, plane_id, status) VALUES (4, 4, 3, '2021-08-04 09:00:00', '2021-08-04 12:00:00', 1, 'Scheduled');
INSERT INTO flights (id, departure_airport_id, arrival_airport_id, departure_time, arrival_time, plane_id, status) VALUES (5, 5, 6, '2021-08-05 09:00:00', '2021-08-05 12:00:00', 2, 'Scheduled');
INSERT INTO flights (id, departure_airport_id, arrival_airport_id, departure_time, arrival_time, plane_id, status) VALUES (6, 6, 5, '2021-08-06 09:00:00', '2021-08-06 12:00:00', 3, 'Scheduled');
-- --- End of file