CREATE TABLE person (
id SERIAL PRIMARY KEY,
name VARCHAR(32),
action VARCHAR(32),
environment_id REFERENCES environment(id)
);

CREATE TABLE sound (
id SERIAL PRIMARY KEY,
grinding BOOL,
indefinite BOOL
);

CREATE TABLE animal (
id SERIAL PRIMARY KEY,
name VARCHAR(32),
age INT,
size VARCHAR(32),
sight VARCHAR(32),
action VARCHAR(32),
horns_size VARCHAR(32),
horns_location VARCHAR(32),
eyes_colour VARCHAR(32),
horns_number_of INT,
horns_colour VARCHAR(32),
eyes_size VARCHAR(32),
nose_horn_is BOOL,
head_size VARCHAR(32),
environment_id REFERENCES environment(id),
sound_id REFERENCES sound(id)
);

CREATE TABLE environment (
id SERIAL PRIMARY KEY,
description VARCHAR(32) 
);

CREATE TABLE youngster (
id SERIAL PRIMARY KEY,
type VARCHAR(32),
action VARCHAR(32)
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

CREATE TABLE person_sound (
person_id INT REFERENCES person(id),
sound_id INT REFERENCES sound(id),
PRIMARY KEY (person_id, animal_id)
);

CREATE OR REPLACE FUNCTION animal_grinding_trigger() RETURNS TRIGGER AS $$
BEGIN
  IF (NEW.age > 50) THEN
    UPDATE sound SET grinding = TRUE WHERE id = NEW.sound_id;
  END IF;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER animal_grinding
AFTER INSERT OR UPDATE ON animal
FOR EACH ROW
EXECUTE FUNCTION animal_grinding_trigger();

CREATE OR REPLACE FUNCTION person_running_away_trigger() RETURNS TRIGGER AS $$
BEGIN
  IF (NEW.grinding = TRUE) THEN
    INSERT INTO person_action (person_id, action)
    UPDATE person SET action = 'running away'
    WHERE id = NEW.person_id;
  END IF;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER person_running_away
AFTER INSERT OR UPDATE ON sound
FOR EACH ROW
EXECUTE FUNCTION person_running_away_trigger();


____

CREATE OR REPLACE FUNCTION run_away() RETURNS TRIGGER AS $$
BEGIN
  IF EXISTS (
    SELECT 1 FROM person_sound
    WHERE person_id = NEW.person_id
    AND sound_id = NEW.sound_id
  ) THEN
    UPDATE person SET action = 'running away'
    WHERE id = NEW.person_id;
  END IF;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER person_sound_trigger
AFTER INSERT ON person_sound
FOR EACH ROW
EXECUTE FUNCTION run_away();
