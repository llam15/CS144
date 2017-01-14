CREATE TABLE Actors (
    Name VARCHAR(40),
    Movie VARCHAR(80),
    Year INT,
    Role VARCHAR(40)
);

LOAD DATA LOCAL INFILE '~/data/actors.csv' INTO TABLE Actors
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"';

SELECT Name FROM Actors WHERE Movie='Die Another Day';

DROP TABLE Actors;