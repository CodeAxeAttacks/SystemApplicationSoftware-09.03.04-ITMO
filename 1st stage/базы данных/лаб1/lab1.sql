DROP TABLE IF EXISTS animal CASCADE;
DROP TABLE IF EXISTS person CASCADE;
DROP TABLE IF EXISTS youngster CASCADE;
DROP TABLE IF EXISTS horns CASCADE;
DROP TABLE IF EXISTS eyes CASCADE;
DROP TABLE IF EXISTS head CASCADE;
DROP TABLE IF EXISTS nose CASCADE;
DROP TABLE IF EXISTS sound CASCADE;
DROP TABLE IF EXISTS environment CASCADE;
DROP TABLE IF EXISTS animal_sound CASCADE;
DROP TABLE IF EXISTS animal_youngster CASCADE;
DROP TABLE IF EXISTS person_animal CASCADE;
DROP TABLE IF EXISTS animal_horns CASCADE;
DROP TABLE IF EXISTS animal_eyes CASCADE;

CREATE TABLE person (
id SERIAL PRIMARY KEY,
name TEXT,
action TEXT,
environment_id INT REFERENCES environment(id)
);

CREATE TABLE sound (
id SERIAL PRIMARY KEY,
grinding BOOL,
indefinite BOOL
);

CREATE TABLE animal (
id SERIAL PRIMARY KEY,
name TEXT,
age TEXT,
size TEXT,
sight TEXT,
action TEXT,
nose_id INT REFERENCES nose(id),
head_id INT REFERENCES head(id),
environment_id INT REFERENCES environment(id)
);

CREATE TABLE environment (
id SERIAL PRIMARY KEY,
description TEXT
);

CREATE TABLE head (
id SERIAL PRIMARY KEY,
size TEXT
);

CREATE TABLE eyes (
id SERIAL PRIMARY KEY,
color TEXT,
size INT
);

CREATE TABLE horns (
id SERIAL PRIMARY KEY,
number_of_horns INT,
horns_color TEXT,
size TEXT,
location TEXT
);

CREATE TABLE nose (
id SERIAL PRIMARY KEY,
horn_is BOOL
);

CREATE TABLE youngster (
id SERIAL PRIMARY KEY,
type TEXT,
action TEXT
);

CREATE TABLE animal_youngster (
animal_id INT REFERENCES animal(id),
youngster_id INT REFERENCES youngster(id),
PRIMARY KEY (animal_id, youngster_id)
);

CREATE TABLE person_animal (
person_id INT REFERENCES person(id),
animal_id INT REFERENCES animal(id),
PRIMARY KEY (person_id, animal_id)
);

CREATE TABLE animal_horns (
animal_id INT REFERENCES animal(id),
horns_id INT REFERENCES horns(id),
PRIMARY KEY (animal_id, horns_id)
);

CREATE TABLE animal_eyes (
animal_id INT REFERENCES animal(id),
eyes_id INT REFERENCES eyes(id),
PRIMARY KEY (animal_id, eyes_id)
);

CREATE TABLE animal_sound (
animal_id INT REFERENCES animal(id),
sounds_id INT REFERENCES sound(id),
PRIMARY KEY (animal_id, sounds_id)
);

INSERT INTO person(name, action, environment_id) VALUES (‘Ralf’, ’Running’, ‘123’)
INSERT INTO sound(grinding, indefinite) VALUES (YES, NO)
INSERT INTO environment(description) VALUES (‘Volley’)
INSERT INTO head(size) VALUES (‘Huge’)
INSERT INTO eyes(color, size) VALUES (‘Red’, ‘Huge’)
INSERT INTO horns(number_of_horns, horns_color, size, location) VALUES (‘2’, ‘White’, ‘Huge’, ‘In the middle’)
INSERT INTO nose(horn_is) VALUES (YES)
INSERT INTO youngster(type, action) VALUES (‘Disabled, ‘NULL’)
INSERT INTO animal(name, age, size, sight, action, nose_id, head_id, eyes_id, horns_id, environment_id) VALUES (‘Daniil’, ‘10’, ‘Giant’, ‘Bright’, ‘1’, ‘11’, ‘111’, ‘1111’, ‘123’)
